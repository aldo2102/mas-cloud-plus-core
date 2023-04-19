/**
 * *************************************************************** 
 * JADE - Java Agent DEvelopment Framework is a framework to develop 
 * multi-agent systems in compliance with the FIPA specifications. 
 * Copyright (C) 2000 CSELT S.p.A. 
 *  
 * GNU Lesser General Public License 
 *  
 * This library is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation, 
 * version 2.1 of the License. 
 *  
 * This library is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * Lesser General Public License for more details. 
 *  
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library; if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, 
 * Boston, MA  02111-1307, USA. 
 * ************************************************************** 
 */

package Agents;

import static jade.core.behaviours.ParallelBehaviour.WHEN_ALL;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;
import jade.wrapper.StaleProxyException;
import models.DadosMonitorados;
import models.ModelsMonitoringForRules;
import models.ModelsProvisioning;

import java.net.InetAddress;
import java.text.SimpleDateFormat;

@SuppressWarnings("unused")
public class ManagerG extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	ParallelBehaviour s = new ParallelBehaviour();
	SequentialBehaviour seqBehaviour = new SequentialBehaviour();

	protected void setup() {
		synchronized (this) {
			System.out.println("Agent " + getLocalName() + " started.");

			addBehaviour(new StartGM());
			addBehaviour(new StartMonitoring());
			/*
			 * try { Thread.sleep(5000); } catch (InterruptedException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 */
			addBehaviour(new StartAPP());

			// s.addSubBehaviour(seqBehaviour);

			System.out.println("Stop Monitoring");
			addBehaviour(new StopMonitoring());
			// addBehaviour(new SerializableMonitoring());

			// s.addSubBehaviour(pr);

		}

	}

	protected void takeDown() {
		System.out.println("Agent " + getAID().getLocalName() + " finishing.");

	}

}

class StartAPP extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void action() {

		Process p = null;

		String finalizado = "";
		try {
			if (Starter.Starter.providerCloud == 2) {
				System.out.println("Executando MASE");

				String[] command = { Starter.Starter.vagrant2, Starter.Starter.vagrant3,
						"gcloud compute ssh instancenew" + Starter.Starter.model.getCpuSelected()
								+ " --zone us-central1-c --command='" + Starter.Starter.command + "'" };

				ProcessBuilder pb = new ProcessBuilder(command);
				p = pb.start();
				OutputStream rsyncStdIn = p.getOutputStream();
				rsyncStdIn.write("aldoaldo".getBytes());
				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line = "";
				String[] lines1;
				while (((line = reader.readLine()) != null)) {

					System.out.println(line);
				}
			}

			if (Starter.Starter.providerCloud == 3 || Starter.Starter.providerCloud == 4) {
				System.out.println("Executando MASE - Vagrant");

				String teste = "";
				int verification = 0;
				// while (!teste.equals("poweroff (virtualbox)") ) {

				String[] command = { Starter.Starter.vagrant2, Starter.Starter.vagrant3,
						"cd " + Starter.Starter.usuarioMasCloud + " && " + Starter.Starter.vagrant + " status" };
				/*
				 * try { Thread.sleep(10000); } catch (InterruptedException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 */
				if (p != null) {
					try {

						// Thread.sleep(10000);
						p.waitFor();
						System.out.println("waitFor");
						p.destroy();
						System.out.println("destroy");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				/*
				 * try { Thread.sleep(2000); } catch (InterruptedException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 */
				// OutputStream rsyncStdIn = p.getOutputStream();
				// rsyncStdIn.write("aldoaldo".getBytes());
				String line = null;
				String[] lines1;
				int count = 0;
				while (true && verification == 0) {

					 try {

							//Thread.sleep(10000);


							ProcessBuilder pb = new ProcessBuilder(command);
							p = pb.start();
							BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
							StringBuilder builder = new StringBuilder();
							while ( (line = reader.readLine()) != null) {
								try {
									 System.out.println(line);
									String split = "default                   ";
									lines1 = line.split(split);

									if (lines1.length > 1 && lines1.length < 3) {
										teste = lines1[1];
										System.out.println(lines1[0] + " - " + lines1[1]);
										if (lines1[1].equals("running (virtualbox)") || lines1[1].equals("running (aws)")
												|| lines1[1].equals("running (google)")) {
											verification = 0;
											System.out.println("11 " + verification);
										} else {
											System.out.println("22 " + verification);
											verification = 1;
											break;
										}
										System.out.println(verification + " Rodando " + count);
									}

									System.out.println("44 " + line);
									count++;

								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.err.println("\f Erro 11" + line);
									e.printStackTrace();
								}
								   builder.append(line);
								   builder.append(System.getProperty("line.separator"));
								}
							line = builder.toString();
							//p.waitFor();
							System.out.println("waitFor");
							//p.destroy();
							System.out.println("destroy");

							 
							 System.out.println(line);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					if (line != null) {
						
					}
					if(verification==1) {
						break;
					}

				}

				if (verification == 1) {
					System.out.println("33 " + verification);
					// break;
				}
				// }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Erro 11");
			e.printStackTrace();
		}
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Starter.Starter.stopPlatform();
			e.printStackTrace();
		}

		// Starter.Starter.stopPlatform();
		Conection();
		System.out.println(finalizado);

	}

	public void Conection() {
		Process p1;

		try {
			if (Starter.Starter.providerCloud == 2) {
				p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 " + Starter.Starter.machine.getIp());
				// System.out.println("ping -c 1 "+Starter.Starter.machine.getIp());
				String[] command2 = { Starter.Starter.vagrant2, Starter.Starter.vagrant3,
						"gcloud compute ssh instancenew" + Starter.Starter.model.getCpuSelected()
								+ " --zone us-central1-c --command='cat executando'" };
				System.out.println("gcloud compute ssh instancenew" + Starter.Starter.model.getCpuSelected()
						+ " --zone us-central1-c --command='cat executando'");
				ProcessBuilder pb = new ProcessBuilder(command2);
				Process pr;
				pr = pb.start();
				OutputStream rsyncStdIn = pr.getOutputStream();
				rsyncStdIn.write("aldoaldo".getBytes());
				String line = "";
				BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				while ((line = reader.readLine()) != null) {
					Starter.Starter.verification = line;
				}
				pr.waitFor();
				int returnVal = p1.waitFor();
				// Thread.sleep(2000);
				boolean reachable = (returnVal == 0);
				if (reachable && Starter.Starter.verification.equals("executando")) {
					System.out.println("Connect");
					Starter.Starter.vm = 1;
				} else {
					Starter.Starter.vm = 0;
					System.out.println("No Connect");
					/*
					 * try { String[] command = { Starter.Starter.vagrant2, Starter.Starter.
					 * vagrant3,"echo 'Y\n' | gcloud compute instances delete instancenew"+Starter.
					 * Starter.model.getCpuSelected()+" --zone 'us-central1-c'"}; pb = new
					 * ProcessBuilder(command); Process pp = pb.start(); rsyncStdIn =
					 * pr.getOutputStream (); rsyncStdIn.write ("aldoaldo".getBytes ()); reader =
					 * new BufferedReader(new InputStreamReader(pp.getInputStream())); line="";
					 * 
					 * try { while ((line = reader.readLine()) != null) { System.out.println(line);
					 * } } catch (NumberFormatException | IOException e) { // TODO Auto-generated
					 * catch block e.printStackTrace(); } pp.waitFor();
					 * 
					 * 
					 * } catch (IOException|InterruptedException e) { // TODO Auto-generated catch
					 * block e.printStackTrace(); }
					 */
				}
			}
			/*
			 * if (Starter.Starter.providerCloud == 3) { p1 =
			 * java.lang.Runtime.getRuntime().exec("ping -c 1 " +
			 * Starter.Starter.machine.getIp()); //
			 * System.out.println("ping -c 1 "+Starter.Starter.machine.getIp()); String[]
			 * command2 = { Starter.Starter.vagrant2, Starter.Starter.vagrant3,
			 * "vagrant ssh -c'cat executando'" };
			 * System.out.println("vagrant ssh -c'cat executando'"); ProcessBuilder pb = new
			 * ProcessBuilder(command2); Process pr; pr = pb.start(); String line = "";
			 * BufferedReader reader = new BufferedReader(new
			 * InputStreamReader(pr.getInputStream())); while ((line = reader.readLine()) !=
			 * null) { Starter.Starter.verification = line; } pr.waitFor(); int returnVal =
			 * p1.waitFor(); Thread.sleep(2000); boolean reachable = (returnVal == 0); if
			 * (reachable && Starter.Starter.verification.equals("executando")) {
			 * System.out.println("Connect"); Starter.Starter.vm = 1; } else {
			 * Starter.Starter.vm = 0; System.out.println("No Connect");
			 * 
			 * } }
			 */

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class StartMonitoring extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void action() {

		System.out.println("Monitoring");
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(new AID("MonitoringAgents", AID.ISLOCALNAME));
		message.setContent("1");
		myAgent.send(message);
		System.out.println("1");
	}

}

class SerializableMonitoring extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void action() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(new AID("MonitoringAgents", AID.ISLOCALNAME));
		message.setContent("3");
		myAgent.send(message);
		System.out.println("3");
	}

}

class StopMonitoring extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void action() {

		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(new AID("MonitoringAgents", AID.ISLOCALNAME));
		message.setContent("2");
		myAgent.send(message);
		System.out.println("2");
		FileWriter arq;
		PrintWriter gravarArq = null;
		synchronized (Starter.Starter.platform) {

			try {
				System.out.println("Monitoramento " + "" + Starter.Starter.usuarioMasCloud + "/" + "base.csv");
				arq = new FileWriter("" + Starter.Starter.usuarioMasCloud + "/" + "base.csv", true);
				gravarArq = new PrintWriter(arq);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MonitoringAgents.tempoInicial = (System.currentTimeMillis() - MonitoringAgents.tempoInicial);

			System.out.println(DadosMonitorados.AVGCPUidl + "," + DadosMonitorados.AVGCPU + ","
					+ Starter.Starter.model.getCpuSelected() + "," + Starter.Starter.transformationAgentQty + ","
					+ DadosMonitorados.getAVGMemory() + "," + Starter.Starter.machine.getMemory() + ","
					+ DadosMonitorados.AVGsendN + "," + DadosMonitorados.AVGrecvN + "," + DadosMonitorados.AVGreadD
					+ "," + DadosMonitorados.AVGwritD + "," + (MonitoringAgents.tempoInicial) + ","
					+ models.ModelsProvisioning.getBestBalance() + "," + Starter.Starter.machine.getCpu() + "\n");

			// System.out.println(DadosMonitorados.AVGMemory + ", " +
			// DadosMonitorados.AVGCPU + ", "
			// + Starter.Starter.machine.getMemory() + ", " +
			// Starter.Starter.machine.getCpu() + "\n");

			if (DadosMonitorados.AVGMemory > 0 && DadosMonitorados.AVGCPU > 0 && Starter.Starter.machine.getMemory() > 0
					&& Starter.Starter.machine.getCpu() > 0) {
				gravarArq.append(DadosMonitorados.AVGCPUidl + "," + DadosMonitorados.AVGCPU + ","
						+ Starter.Starter.machine.getCpu() + "," + Starter.Starter.transformationAgentQty + ","
						+ DadosMonitorados.getAVGMemory() + "," + Starter.Starter.machine.getMemory() + ","
						+ DadosMonitorados.AVGsendN + "," + DadosMonitorados.AVGrecvN + "," + DadosMonitorados.AVGreadD
						+ "," + DadosMonitorados.AVGwritD + "," + (MonitoringAgents.tempoInicial) + ","
						+ models.ModelsProvisioning.getBestBalance() + "," + ModelsProvisioning.getSize() + ","
						+ ModelsProvisioning.getPriceSelected() + "\n");
			}
			gravarArq.close();
			System.out.println("StopMonitoring");
			// Starter.Starter.platform.notifyAll();

		}
		// Starter.Starter.stopPlatform();
		this.takeDown();
	}

	protected void takeDown() {

		System.out.println("Agent2 " + getAgent().getLocalName() + " finishing.");

	}

}

class StartGM extends OneShotBehaviour {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	public void action() {
		AgentController novoAgent = null;
		Starter.Starter.platform = myAgent.getContainerController();

		try {
			novoAgent = Starter.Starter.platform.createNewAgent("MonitoringAgents", "Agents.MonitoringAgents", null);
		} catch (ControllerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			novoAgent.start();
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void takeDown() {

	}

}
