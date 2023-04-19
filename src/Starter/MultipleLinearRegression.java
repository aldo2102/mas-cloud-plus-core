package Starter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;

import com.opencsv.CSVReader;

import Jama.Matrix;
import Jama.QRDecomposition;
import models.ModelsProvisioning;

public class MultipleLinearRegression {

	int N; // number of
	int p; // number of dependent variables
	Matrix beta; // regression coefficients
	private double SSE; // sum of squared
	private double SST; // sum of squared

	public MultipleLinearRegression(String base) {
		action(base);
	}
	
	public MultipleLinearRegression() {}

	public MultipleLinearRegression(double[][] x, double[] y) {
		if (x.length != y.length)
			throw new RuntimeException("dimensions don't agree");
		N = y.length;
		p = x[0].length;

		Matrix X = new Matrix(x);

		// create matrix from vector
		Matrix Y = new Matrix(y, N);

		// find least squares solution
		QRDecomposition qr = new QRDecomposition(X);
		// System.out.println(Y.toString().toString()+" "+N+" "+y.length);

		beta = qr.solve(Y);

		// mean of y[] values
		double sum = 0.0;
		for (int i = 0; i < N; i++)
			sum += y[i];
		double mean = sum / N;

		// total variation to be accounted for
		for (int i = 0; i < N; i++) {
			double dev = y[i] - mean;
			SST += dev * dev;
		}

		// variation not accounted for
		Matrix residuals = X.times(beta).minus(Y);
		SSE = residuals.norm2() * residuals.norm2();

	}

	public double beta(int j) {
		return beta.get(j, 0);
	}

	public double R2() {
		return 1.0 - SSE / SST;
	}
	
	

	public void action(String base) {

		double x[][] = null;
		double y[] = null;
		double x1[][] = null;
		double y1[] = null;
		double x2[][] = null;
		double y2[] = null;
		double x3[][] = null;
		double y3[] = null;
		double x4[][] = null;
		double y4[] = null;
		double x5[][] = null;
		double y5[] = null;
		
		String[] nextLine;
		String strFile;
		CSVReader reader = null;
		int lineNumber = 0;
		int cont = 1;
		try {
			// csv file containing data
			strFile = base;
			/*
			 * reader = new CSVReader(new FileReader(strFile)); reader.close();
			 */
			reader = new CSVReader(new FileReader(strFile));
			while ((nextLine = reader.readNext()) != null) {
				lineNumber++;
			}

			x = new double[lineNumber][3];
			y = new double[lineNumber];
			x1 = new double[lineNumber][3];
			y1 = new double[lineNumber];
			x2 = new double[lineNumber][3];
			y2 = new double[lineNumber];
			
			x3 = new double[lineNumber][3];
			y3 = new double[lineNumber];
			x4 = new double[lineNumber][3];
			y4 = new double[lineNumber];
			x5 = new double[lineNumber][3];
			y5 = new double[lineNumber];
			
			reader = new CSVReader(new FileReader(strFile));
			lineNumber = 0;
			while ((nextLine = reader.readNext()) != null) {

				// nextLine[] is an array of values from the line

				if (nextLine.length > 1) {
					if (Character.isDigit(nextLine[0].charAt(0))) {
						double CPUNotUsed = (double) Double.parseDouble(nextLine[0]);
						//long agents = (long)  (Double.parseDouble(nextLine[4]));  //Integer.parseInt(nextLine[3]);
						double vCPU = (double) (Double.parseDouble(nextLine[2]));
						double CPUUsed = (double) Double.parseDouble(nextLine[1]);
						double time = (double) Double.parseDouble(nextLine[10]);
						double memoryUsed = (double)  Double.parseDouble(nextLine[4]);
						double memory = (double)  Double.parseDouble(nextLine[5])/1000000;
						memoryUsed = ((memoryUsed/1000)*100)/ memory; // Porcentagem de uso

						
						if(memory>0 && vCPU>0 && time>0) {
							x[lineNumber][0] = 1;
							x[lineNumber][1] = Math.log10(Math.abs(memory));
							x[lineNumber][2] = Math.log10(Math.abs(vCPU));
							y[lineNumber] = Math.log10(time);
							
							x1[lineNumber][0] = 1;
							x1[lineNumber][1] = memory;
							x1[lineNumber][2] =  vCPU;
							y1[lineNumber] = CPUUsed;
							
							x2[lineNumber][0] = 1;
							x2[lineNumber][1] = Math.log10(Math.abs(memory));
							x2[lineNumber][2] =  Math.log10(Math.abs(vCPU));
							y2[lineNumber] = Math.log10(Math.abs(memoryUsed));
						
						//Com logs
							x3[lineNumber][0] = 1;
							x3[lineNumber][1] = Math.log10(Math.abs(memory));
							x3[lineNumber][2] = Math.log10(Math.abs(vCPU));
							y3[lineNumber] = time;
							
							x4[lineNumber][0] = 1;
							x4[lineNumber][1] = Math.log10(Math.abs(memory));
							x4[lineNumber][2] = Math.log10(Math.abs(vCPU));
							y4[lineNumber] = Math.log10(Math.abs(CPUUsed));
							
							x5[lineNumber][0] = 1;
							x5[lineNumber][1] = Math.log10(Math.abs(memory));
							x5[lineNumber][2] = Math.log10(Math.abs(vCPU));
							y5[lineNumber] = Math.log10(Math.abs(memoryUsed));
						}

					}
					// System.out.println(nextLine[0] +" "+nextLine[1]+" "+nextLine[2]+"
					// "+nextLine[3]+" "+nextLine[4]);
				}
				lineNumber++;
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		System.out.println("Regressao ");
		MultipleLinearRegression regression = new MultipleLinearRegression(x, y);

		System.out.printf("Time = %.2f + %.2f beta1 + %.2f beta2  (R^2 = %.2f)\n", regression.beta(0),
				regression.beta(1), regression.beta(2), /*regression.beta(3),*/ regression.R2());

		System.out.println("Regressao 1");
		MultipleLinearRegression regression1 = new MultipleLinearRegression(x1, y1);

		System.out.printf("CPU Used = %.2f + %.2f beta1 + %.2f beta2 + (R^2 = %.2f)\n",
				regression1.beta(0), regression1.beta(1), regression1.beta(2), /*regression1.beta(3),*/ regression1.R2());

		System.out.println("Regressao 2");
		MultipleLinearRegression regression2 = new MultipleLinearRegression(x2, y2);

		System.out.printf("Memory Used = %.2f + %.2f beta1 + %.2f beta2  (R^2 = %.2f)\n", regression2.beta(0),
				regression2.beta(1), regression2.beta(2), regression2.R2());

		System.out.println("Regressao 3");
		MultipleLinearRegression regression3 = new MultipleLinearRegression(x3, y3);

		System.out.printf("Log CPU Time with CPU = %.2f + %.2f beta1 + %.2f beta2    (R^2 = %.2f)\n",
				regression3.beta(0), regression3.beta(1), regression3.beta(2), regression3.R2());

		System.out.println("Regressao 4");
		MultipleLinearRegression regression4 = new MultipleLinearRegression(x4, y4);

		System.out.printf("Log Memory with CPU = %.2f + %.2f beta1 + %.2f beta2   (R^2 = %.2f)\n",
				regression4.beta(0), regression4.beta(1), regression4.beta(2), regression4.R2());
		
		System.out.println("Regressao 5");
		MultipleLinearRegression regression5 = new MultipleLinearRegression(x5, y5);
		
		System.out.printf("Log Memory with CPU = %.2f + %.2f beta1 + %.2f beta2   (R^2 = %.2f)\n",
				regression5.beta(0), regression5.beta(1), regression5.beta(2), regression5.R2());
		

		FileWriter arq2 = null;
		PrintWriter gravarArq2 = null;
		try {
			String localTime = java.time.Clock.systemUTC().instant().toString();
			
			arq2 = new FileWriter("baseRegression"+Starter.usuarioMasCloud+".csv", true);

			gravarArq2 = new PrintWriter(arq2);

			gravarArq2.append("Time " + regression.beta(0) + " " + regression.beta(1) + " beta1 " + regression.beta(2)
					+ " beta2 + " /*+ regression.beta(3) + " beta3 ;*/+"  R^2 " + regression.R2() + "\n");

			gravarArq2.append("CPU " + regression1.beta(0) + " " + regression1.beta(1) + " beta1 " + regression1.beta(2)
					+ " beta2 " + /*regression1.beta(3) + " beta3  ;*/"    R^2 " + regression1.R2() + "\n");

			gravarArq2.append(
					"Memory " + regression2.beta(0) + " " + "+ " + regression2.beta(1) + " beta1 " + regression2.beta(2)
							+ " beta2 " + /*regression4.beta(3) + " beta3 ;*/"    R^2 " + regression2.R2() + "\n");
			arq2.close();
			gravarArq2.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ModelsProvisioning.setR2Time(regression.R2());
		//ModelsProvisioning.setR2CPU(regression1.R2());
		double time = Double.MAX_VALUE;
		double timeTemp = 0, cpuTemp = 0, memoryTemp = 0;
		// csv file containing data
		strFile = base;
		try {
			reader = new CSVReader(new FileReader(strFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double t1 = 0, sumNoUsed = 0;
		int contator = 1;
		ModelsProvisioning.setCpuAvg(0);
		ModelsProvisioning.setCpuSum(0);
		cont = 0;
		
		
		if(Starter.providerCloud==3) {

			PriceAWS pa = new PriceAWS();
			
			pa.prices();


			/*try {
				//PriceGoogleCloud.prices();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
		}
		
		if(Starter.providerCloud==4) {

			PriceAzure pa = new PriceAzure();
			
			pa.prices();
		}

		/*double cpus[] = new double[Starter.MV.size()];
		double memoirs[] = new double[Starter.MV.size()];

		for(int z = 0;z < Starter.MV.size();z++) {
			cpus[z]=Starter.MV.get(z).getvCPU();
			memoirs[z]=Starter.MV.get(z).getvCPU();
		}
		*/
		double cpus[] = {4,8,12};
		double memoirs[] = {16,32,48};
		
		while (cont < cpus.length) {


			Starter.model = new ModelsProvisioning();

			timeTemp=Math.pow(10,regression.beta(0) + regression.beta(1)*Math.log10(memoirs[cont])+
					regression.beta(2)*Math.log10(cpus[cont]));
		
			cpuTemp=(regression1.beta(0) + regression1.beta(1)*(memoirs[cont])+regression1.beta(2)*(cpus[cont])>0?
					regression1.beta(0) + regression1.beta(1)*memoirs[cont]+regression1.beta(2)*cpus[cont]:5);
			
			
			memoryTemp = ((Math.pow(10,regression2.beta(0) + regression2.beta(1) * Math.log10(memoirs[cont]) + 
					regression2.beta(2) * Math.log10(cpus[cont]))>0)?
					Math.pow(10,regression2.beta(0) + regression2.beta(1) * Math.log10(memoirs[cont]) + 
							regression2.beta(2) * Math.log10(cpus[cont])):0.00001);
			
			
			t1 = ModelsProvisioning.getCpuSum() + cpuTemp;
			ModelsProvisioning.setCpuSum(t1);
			ModelsProvisioning.setCpuAvg(ModelsProvisioning.getCpuSum() / contator);
			Starter.model.setCpuUSED(cpuTemp);
			Starter.model.setMemoryUSED(memoryTemp);

			Starter.model.setCpu((int)cpus[cont]);
			Starter.model.setTime(timeTemp);

			Double priceReal = Double.MAX_VALUE;
			
			
			for(int k=0;k<Starter.MV.size();k++) {
				int cpu = (int) Starter.MV.get(k).getvCPU();
				double memoria = Starter.MV.get(k).getMemory();

				if(cpu ==  cpus[cont] && memoria <= cpus[cont]/0.25) {
					priceReal = Starter.MV.get(k).getPrice();
				}
			}
				
			
			
			Starter.model.setPrice(priceReal);
			
			
			Starter.model.setBalance(((memoryTemp + cpuTemp) / ((priceReal + timeTemp*0.4)*cpus[cont])*100000));
			
			sumNoUsed = sumNoUsed + Starter.model.getCpuNoUsed();
			ModelsProvisioning.setCpuNoUsedAvg(sumNoUsed / contator);

			if (ModelsProvisioning.getCpuMax() < cpuTemp) {
				ModelsProvisioning.setCpuMax(cpuTemp);

				
			}
			Starter.valuesCpus.add(Starter.model);

			System.out.println( " ------- # --------");
			System.out.println( " CPU " + Starter.model.getCpuUSED() + " CPU " + cpus[cont]);
			System.out.println( " AVG " + Starter.model.getCpuAvg() + " max " + Starter.model.getCpuMax() + " tempo "+Starter.model.getTime());
			System.out.println( " Balan�o " + Starter.model.getBalance() + " Pre�o "+ Starter.model.getBalance());
			System.out.println( " Memory use " +Starter.model.getMemoryUSED());
			System.out.println( " Price " +Starter.model.getPrice());
			System.out.println( " ------- # --------");
			
			
			cont++;
			

		}
		
		System.out.println("CPUs "+Starter.valuesCpus+" - "+Starter.valuesCpus.size());
		for (int i = 0; i < Starter.valuesCpus.size(); i++) {

			ModelsProvisioning m = (ModelsProvisioning) Starter.valuesCpus.get(i);


			if (i == 0) {
				Starter.model.setTimeSelected(Double.MAX_VALUE);
				Starter.model.setPriceSelected(Double.MAX_VALUE);
				m.setCpu(Starter.valuesCpus.get(i).getCpu());
				m.setMemory(Starter.valuesCpus.get(i).getMemory());
			} else {
				m.setCpu(Starter.valuesCpus.get(i).getCpu());
				m.setMemory(Starter.valuesCpus.get(i).getMemory());
			}

			try {
					System.out.println("Decisao "+Starter.escolhaDecisao);
					if (Starter.escolhaDecisao == 1 ) {
						/*System.out.println("Antes ");
						
						System.out.println(ModelsProvisioning.getPriceSelected() >= m.getPrice());
						System.out.println(ModelsProvisioning.getTimeSelected() >= m.getTime());
						System.out.println((ModelsProvisioning.getCpuUsedSelected() * 1) < m.getCpuUSED());
						System.out.println(ModelsProvisioning.getMemoryUSEDSelected() * 1 < m.getMemoryUSED());
						System.out.println((m.getMemoryUSED() < 1)+" "+m.getMemoryUSED());
						System.out.println((m.getCpuUSED() <= 5)+" "+m.getCpuUSED());*/
						 
						
						Starter.kSession.insert(m);
						Starter.kSession.fireAllRules();
						//System.out.println("Depis ");
					}
	
					if (Starter.escolhaDecisao == 2 ) {
						System.out.println("PYTHON ----------------");
						m = executarPython(m,base);
						break;
					}
					if (Starter.escolhaDecisao == 3 ) {
						Grasp.grasp();
						break;
					}
				

			} catch (Throwable t) {
				t.printStackTrace();
			}

		}
		
		//System.exit(1);

		System.out.println("Tamanho "+ModelsProvisioning.getCpusCandidatesSize());
		ModelsProvisioning.setBestBalance(Double.MIN_VALUE);
		for (int i = 0; i < ModelsProvisioning.getCpusCandidatesSize(); i++) {
			 System.out.println("CPU "+ModelsProvisioning.getCpusCandidates(i).getCpu()+
					 "Tempo "+ModelsProvisioning.getCpusCandidates(i).getTime());
			try {

				System.out.println(ModelsProvisioning.getBestBalance()+" "+ Starter.model.getCpusCandidates(i).getBalance());
				if (Starter.escolhaDecisao == 1) {
					Starter.kSession.insert(i);
					Starter.kSession.fireAllRules();
				}

			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		FileWriter arq;
		PrintWriter gravarArq = null;

		try {
			arq = new FileWriter("statsCPUSelected.csv", true);
			gravarArq = new PrintWriter(arq);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gravarArq.append(Starter.transformationAgentQty + "," + ModelsProvisioning.getCpuUsedSelected() + "\n");
		gravarArq.close();

		// System.out.println(ModelsProvisioning.getCpusCandidates()+" KKKKK
		// "+Starter.transformationAgentQty + "," +Starter.model.getCpuSelected());

	}
	
	        

	  public ArrayList<MultipleLinearRegression> action2(String base) {

	      double x[][] = null;
	      double y[] = null;
	      double x1[][] = null;
	      double y1[] = null;
	      double x2[][] = null;
	      double y2[] = null;
	      double x3[][] = null;
	      double y3[] = null;
	      double x4[][] = null;
	      double y4[] = null;
	      double x5[][] = null;
	      double y5[] = null;
	      
	      String[] nextLine;
	      String strFile;
	      CSVReader reader = null;
	      int lineNumber = 0;
	      int cont = 1;


	try {
				// csv file containing data
				strFile = base;
				/*
				 * reader = new CSVReader(new FileReader(strFile)); reader.close();
				 */
				reader = new CSVReader(new FileReader(strFile));
				while ((nextLine = reader.readNext()) != null) {
					lineNumber++;
				}

				x = new double[lineNumber][3];
				y = new double[lineNumber];
				x1 = new double[lineNumber][3];
				y1 = new double[lineNumber];
				x2 = new double[lineNumber][3];
				y2 = new double[lineNumber];
				
				x3 = new double[lineNumber][3];
				y3 = new double[lineNumber];
				x4 = new double[lineNumber][3];
				y4 = new double[lineNumber];
				x5 = new double[lineNumber][3];
				y5 = new double[lineNumber];
				
				reader = new CSVReader(new FileReader(strFile));
				lineNumber = 0;
				while ((nextLine = reader.readNext()) != null) {

					// nextLine[] is an array of values from the line

					if (nextLine.length > 10) {
						if (Character.isDigit(nextLine[0].charAt(0)) ) {
							double CPUNotUsed = (double) Double.parseDouble(nextLine[0]);
							//long agents = (long)  (Double.parseDouble(nextLine[4]));  //Integer.parseInt(nextLine[3]);
							double vCPU = (double) (Double.parseDouble(nextLine[2]));
							double CPUUsed = (double) Double.parseDouble(nextLine[1]);
							double time = (double) Double.parseDouble(nextLine[10]);
							double memoryUsed = (double)  Double.parseDouble(nextLine[4]);
							double memory = (double)  Double.parseDouble(nextLine[5])/1000000;
							memoryUsed = ((memoryUsed/1000)*100)/ memory; // Porcentagem de uso

							
							if(memory>0 && vCPU>0 && time>0) {
								x[lineNumber][0] = 1;
								x[lineNumber][1] = Math.log10(Math.abs(memory));
								x[lineNumber][2] = Math.log10(Math.abs(vCPU));
								y[lineNumber] = Math.log10(time);
								
								x1[lineNumber][0] = 1;
								x1[lineNumber][1] = memory;
								x1[lineNumber][2] =  vCPU;
								y1[lineNumber] = CPUUsed;
								
								x2[lineNumber][0] = 1;
								x2[lineNumber][1] = Math.log10(Math.abs(memory));
								x2[lineNumber][2] =  Math.log10(Math.abs(vCPU));
								y2[lineNumber] = Math.log10(Math.abs(memoryUsed));
							
							//Com logs
								x3[lineNumber][0] = 1;
								x3[lineNumber][1] = Math.log10(Math.abs(memory));
								x3[lineNumber][2] = Math.log10(Math.abs(vCPU));
								y3[lineNumber] = time;
								
								x4[lineNumber][0] = 1;
								x4[lineNumber][1] = Math.log10(Math.abs(memory));
								x4[lineNumber][2] = Math.log10(Math.abs(vCPU));
								y4[lineNumber] = Math.log10(Math.abs(CPUUsed));
								
								x5[lineNumber][0] = 1;
								x5[lineNumber][1] = Math.log10(Math.abs(memory));
								x5[lineNumber][2] = Math.log10(Math.abs(vCPU));
								y5[lineNumber] = Math.log10(Math.abs(memoryUsed));
							}

						}
						// System.out.println(nextLine[0] +" "+nextLine[1]+" "+nextLine[2]+"
						// "+nextLine[3]+" "+nextLine[4]);
					}
					lineNumber++;
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

	    //System.out.println("Regressao ");
			MultipleLinearRegression regression = new MultipleLinearRegression(x, y);

			MultipleLinearRegression regression1 = new MultipleLinearRegression(x1, y1);

			MultipleLinearRegression regression2 = new MultipleLinearRegression(x2, y2);

			MultipleLinearRegression regression3 = new MultipleLinearRegression(x3, y3);

			MultipleLinearRegression regression4 = new MultipleLinearRegression(x4, y4);

			MultipleLinearRegression regression5 = new MultipleLinearRegression(x5, y5);
			

	    ArrayList<MultipleLinearRegression> ar =  new ArrayList<MultipleLinearRegression>();

	    ar.add(regression);
	    ar.add(regression1);
	    ar.add(regression2);

	    return ar;


	  }

	Process mProcess;

	public static ModelsProvisioning executarPython(ModelsProvisioning m, String base) {
		try {

			int number1 = 0;
			
	        
	        
	        ProcessBuilder pb = new ProcessBuilder("python3", "otimizacaoOrtoos.py",  base,"1","1","1");
			

			pb.command("python3", "otimizacaoOrtoos.py",  base,"1","1","1");

			pb.redirectErrorStream(true);

			Process p = pb.start();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

			
			double vCPU = 0;
			double memoria = 0;
			String s = "";
			while ((s = in.readLine()) != null) {
				System.out.println(s);
				try {
					s=s.trim().replaceAll("0;0","");
					s=s.trim().replaceAll("\\s+"," ");
					String[] parts = s.split(" ");

					if(parts[0] == "Tempo") {
			            double tempo = Double.parseDouble(parts[1]);
			            ModelsProvisioning.setTimeSelected(tempo);
			            ModelsProvisioning.setTimesCandidates(tempo);
			            Starter.model.setTimeSelected(tempo);
			            Starter.model.setTimesCandidates(tempo);
					}
			        else if(parts[0] == "Custo") {
			            double price = Double.parseDouble(parts[1]);
						ModelsProvisioning.setPriceSelected(price);
			            Starter.model.setPriceSelected(price);
			        }
			        else if(parts[0] == "Prioridade") {
			        	double prioridade = Double.parseDouble(parts[1]);
			        }
			        else if(parts[0].equals("vCPU")) {
			        	
			        	vCPU= Double.parseDouble(parts[1]);
			        	Starter.model.setCpuSelected((int)vCPU);
			        	Starter.model.setCpu((int)vCPU);
			        	Starter.machine.setCpu((int)vCPU);

			        	System.out.println(Starter.model.getCpuSelected());
			        	System.out.println(Starter.model.getCpu());
			        	System.out.println(Starter.machine.getCpu());
			        }
			        else if(parts[0] == "Memoria") {
			        	memoria = Double.parseDouble(parts[1]);
						ModelsProvisioning.setMemoryUSEDSelected(memoria);
						Starter.model.setMemoryUSEDSelected(memoria);
			        	Starter.machine.setMemory((float)memoria);
			        }
					
				} catch (Exception e) {

					System.out.println(e);
				}
			}

		} catch (Exception e) {

			System.out.println(e);
		}
		

		
		

		double memory = m.getMemoryUSED() * Starter.cpuUsageVariable;
		double cpu = m.getCpuUSED() * Starter.cpuUsageVariable;
		double price = m.getPrice() * Starter.priceVariable;

		double val = memory + cpu + price;

		m.setBalance(val);

		//System.out.println("Drools regra Balanco price " + m.getBalance() + " CPU " + m.getMemoryUSED());
		ModelsProvisioning.setCpusCandidates(m);
		//System.out.println("Drools regra Balanco price " + m.getBalance() + " CPU " + m.getMemoryUSED());
		return m;
	}
}
