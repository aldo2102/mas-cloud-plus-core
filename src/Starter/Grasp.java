package Starter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.opencsv.CSVReader;

import java.util.*;
import Jama.Matrix;
import Jama.QRDecomposition;
import models.ModelPrices;
import models.ModelsConfigMachines;
import models.ModelsProvisioning;

import com.opencsv.exceptions.CsvException;

public class Grasp {

	private static List<ModelPrices> prices = new ArrayList<ModelPrices>();

	private static String strFile;
	private static double time=0;
	private static double CPUusage=0;
	private static double Menusage=0;

	public static void grasp() {

		// System.out.println(prices.size());
		strFile = Starter.strFile;
		System.out.println(strFile);
		// strFile = "10k.csv";
		// strFile = "50k.csv";
		// strFile = "150k.csv";
		// strFile = "500k.csv";
		// strFile = "1M.csv";
		int count = 0;
		/*double time = 0;
		double CPUusage = 0;
		double Menusage = 0;
*/
		try (CSVReader reader = new CSVReader(new FileReader(strFile))) {
			String[] lineInArray;
			while ((lineInArray = reader.readNext()) != null) {
				if (lineInArray.length > 10) {
					//System.out.println(lineInArray.length + " " + lineInArray[0]+" "+CPUusage);
					time = Double.parseDouble(lineInArray[10]) + time;
					CPUusage = Double.parseDouble(lineInArray[0]) + CPUusage;
					double x = Double.parseDouble(lineInArray[5]) / 1000000;
					Menusage = (100 - (((Double.parseDouble(lineInArray[4]) / 1000) * 100) / x)) + Menusage;

					count++;
					//System.out.println(count);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		time = time / count;
		CPUusage = CPUusage / count;
		Menusage = Menusage / count;
		System.out.println("Começa aqui "+time + " " + CPUusage + " " + Menusage + " "+count);
		FileReader reader;

		if (Starter.providerCloud == 3) {
			/*try {
				reader = new FileReader("azureprice-export.json");

				JSONTokener tokener = new JSONTokener(reader);
				JSONObject jsonString = new JSONObject(tokener);

				JSONArray rows = (JSONArray) jsonString.get("mv");

				for (int i = 0; i < rows.length(); i++) {
					// System.out.println(rows.getJSONObject(i).get("bestPriceRegion").toString());
					if (rows.getJSONObject(i).get("bestPriceRegion").toString().equals("East US / 0")) {

						// System.out.println(rows.getJSONObject(i).get("bestPriceRegion").toString());
						ModelPricesAWS item = new ModelPricesAWS();
						item.setSize(rows.getJSONObject(i).get("name").toString());
						item.setPrice(Double.parseDouble(rows.getJSONObject(i).get("linuxPrice").toString()));
						item.setvCPU(Double.parseDouble(rows.getJSONObject(i).get("numberOfCores").toString()));
						item.setMemory(Double.parseDouble(rows.getJSONObject(i).get("memoryInMB").toString()) / 1000);
						item.setProvider(1);
						prices.add(item);

					}

				}
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}*/

		}
		
		JSONObject jsonArray = null;
		JSONArray jsonArray2 = null;
		if (Starter.providerCloud == 3) {
			try {

				reader = new FileReader("aws-linux-od.json");

				JSONTokener tokener = new JSONTokener(reader);
				JSONObject jsonString = new JSONObject(tokener);

				// System.out.println(jsonString.get("config"));

				jsonArray = (JSONObject) jsonString.get("config");

				JSONObject row = (JSONObject) jsonString.get("config");
				// JSONArray row = row.getJSONObject("config");
				JSONArray jsonArray1 = row.getJSONArray("regions");

				JSONArray jsonArray3;
				JSONArray jsonArray4;
				for (int i = 0; i < jsonArray1.length(); i++) {

					jsonArray4 = jsonArray1.getJSONObject(i).getJSONArray("instanceTypes");
					for (int k = 0; k < jsonArray4.length(); k++) {

						jsonArray2 = jsonArray4.getJSONObject(k).getJSONArray("sizes");
						for (int j = 0; j < jsonArray2.length(); j++) {

							jsonArray3 = jsonArray2.getJSONObject(j).getJSONArray("valueColumns");
							jsonArray3 = new JSONArray(
									"[" + jsonArray3.getJSONObject(0).get("prices").toString() + "]");

							try {
								ModelPrices item = new ModelPrices();
								// item.setRegion(jsonArray.getJSONObject(i).get("region").toString());
								item.setSize(jsonArray2.getJSONObject(j).get("size").toString());
								item.setPrice(Double.parseDouble(jsonArray3.getJSONObject(0).get("USD").toString()));
								item.setvCPU(Double.parseDouble(jsonArray2.getJSONObject(j).get("vCPU").toString()));
								item.setMemory(
										Double.parseDouble(jsonArray2.getJSONObject(j).get("memoryGiB").toString()));
								item.setProvider(2);
								prices.add(item);
								

							} catch (NumberFormatException e) {
								// System.out.println(jsonArray3.getJSONObject(0).get("USD").toString());
							}
						}
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				//PriceGoogleCloud.prices();
				double cpus[] = {4,8,12};
				double memoirs[] = {16,32,48};

				int cont = 0;
				 

				Double priceReal = Double.MAX_VALUE;
				
				while (cont < cpus.length) {
					
					ModelPrices item = new ModelPrices();
					//item.setRegion(jsonArray.getJSONObject(i).get("region").toString());
					//item.setSize(jsonArray2.getJSONObject(j).get("size").toString());
	                item.setvCPU(cpus[cont]);
	                item.setMemory(memoirs[cont]);
	                
	                priceReal = cpus[cont]*Starter.prices.getCpuPrice() + memoirs[cont]*Starter.prices.getMemoryPrice();

	                item.setPrice(priceReal);
	                Starter.MV.add(item);

					System.out.println(Starter.MV.size());
					//for(int k=0;k<Starter.MV.size();k++) {
						
						
						ModelPrices item21 = new ModelPrices();
						// item.setRegion(jsonArray.getJSONObject(i).get("region").toString());
						//item.setSize(jsonArray2.getJSONObject(j).get("size").toString());
						item21.setPrice(priceReal);
						item21.setvCPU(cpus[cont]);
						item21.setMemory(memoirs[cont]);
						item21.setProvider(2);
						prices.add(item21);
					//}

					cont++;
					
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		
		
		Collections.sort(prices);

		Random gerador = new Random();

		int construction = gerador.nextInt(prices.size());
		int constructionaux = 0;
		double best = Double.MAX_VALUE;
		int bestPos = 0;
		System.out.println("Primeiro " + zFuncion(constructionaux));
		for (int i = 0; i < 50; i++) {
			constructionaux = seachLocal(construction);

			/*System.out.println("constructionaux "+constructionaux);
			System.out.println("construction "+construction);
			System.out.println("best "+best);

			System.out.println("zFuncion(constructionaux) "+zFuncion(constructionaux));*/
			if (zFuncion(constructionaux) <= best) {
				best = zFuncion(constructionaux);
				bestPos = constructionaux;
			}

			System.out.println("Beste " + best + " " + construction);

			int construction1 = gerador.nextInt(prices.size());

			if (construction1 + construction <= prices.size()) {
				construction = construction1 + construction;
			}
			if (construction1 - construction >= 0) {
				construction = construction1 - construction;
			} else {
				construction = construction1;
			}
		}

		Starter.model.setCpuSelected((int) prices.get(bestPos).getvCPU());
		Starter.model.setCpu((int) prices.get(bestPos).getvCPU());
		Starter.machine.setCpu((int) prices.get(bestPos).getvCPU());
		
		/*
		 * ModelsProvisioning.setTimeSelected(prices.get(bestPos).get);
		 * ModelsProvisioning.setTimesCandidates(tempo);
		 * Starter.model.setTimeSelected(tempo);
		 * Starter.model.setTimesCandidates(tempo);
		 */
		Starter.model.setSize(prices.get(bestPos).getSize());
		ModelsProvisioning.setPriceSelected(prices.get(bestPos).getPrice());
		Starter.model.setPriceSelected(prices.get(bestPos).getPrice());
		ModelsProvisioning.setMemoryUSEDSelected(prices.get(bestPos).getMemory());
		Starter.model.setMemoryUSEDSelected(prices.get(bestPos).getMemory());
		Starter.machine.setMemory((float) prices.get(bestPos).getMemory());

		System.out.println("Máquina " + gerador.nextInt(prices.size()));
		System.out.println(prices.get(bestPos).getvCPU() + " " + prices.get(bestPos).getMemory() + " "
				+ prices.get(bestPos).getProvider());// +" "+timeTemp[bestPos]+" "+cpuTemp[bestPos]+"
														// "+memoryTemp[bestPos]+" "+prices.get(bestPos).getProvider()+"
														// "+zFuncion(bestPos);

	}

	public static int seachLocal(int construction) {

		double custoTempo = zFuncion(construction);

		int constructionAux = construction;
		int cont = 1;

		try {
			//System.out.println("estou aqui");
			double custoTempoAux = zFuncion(constructionAux + cont);
	
			if (constructionAux + cont <= prices.size()) {
	
				while (custoTempoAux < custoTempo || cont <= 5) {
					if (custoTempoAux < custoTempo) {
						construction = constructionAux + cont;
						// System.out.println(cont+". Construção "+custoTempo);
					}
					cont++;
					if (constructionAux + cont >= prices.size()) {
						break;
					}
					custoTempo = zFuncion(construction);
					custoTempoAux = zFuncion(constructionAux + cont);
	
				}
			}

			//System.out.println("estou aqui2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			cont = 1;

			//System.out.println("estou aqui3");
			custoTempo = zFuncion(construction);
			double custoTempoAux = zFuncion(constructionAux + cont);
			if (constructionAux - cont >= 0) {
	
				while (custoTempoAux < custoTempo || cont <= 5) {
					if (custoTempoAux < custoTempo) {
						construction = constructionAux - cont;
						// System.out.println(cont+". Construção "+custoTempo+" "+construction);
					}
	
					cont++;
					if (constructionAux - cont >= 0) {
						break;
					}
					custoTempo = zFuncion(construction);
					custoTempoAux = zFuncion(constructionAux + cont);
	
				}
			}

			//System.out.println("estou aqui4");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return construction;
	}

	static double zFuncion(int cont) {

		MultipleLinearRegression prov = new MultipleLinearRegression();

		ArrayList<MultipleLinearRegression> regressoes = prov.action2(strFile);

		double cpus[] = new double[Starter.MV.size()];
		double memoirs[] = new double[Starter.MV.size()];

		for (int z = 0; z < Starter.MV.size(); z++) {
			cpus[z] = Starter.MV.get(z).getvCPU();
			memoirs[z] = Starter.MV.get(z).getvCPU();
		}

		double timeTemp[] = new double[prices.size()];
		double cpuTemp[] = new double[prices.size()];
		double memoryTemp[] = new double[prices.size()];
		double pricesTemp[] = new double[prices.size()];

		double alfaT = 0.33;
		double alfaC = 0.33;
		double alfaD = 0.33;

		double custoTemp[] = new double[prices.size()];
		
		System.out.println("COUNT "+cont+" "+prices.size());
		timeTemp[cont] = Math.pow(10,
				regressoes.get(0).beta(0) + regressoes.get(0).beta(1) * Math.log10(prices.get(cont).getMemory())
						+ regressoes.get(0).beta(2) * Math.log10(prices.get(cont).getvCPU()));

		pricesTemp[cont] = timeTemp[cont] * prices.get(cont).getPrice();

		cpuTemp[cont] =  (regressoes.get(1).beta(0) + regressoes.get(1).beta(1) * (prices.get(cont).getMemory())
				+ regressoes.get(1).beta(2) * (prices.get(cont).getvCPU()) > 0
						? regressoes.get(1).beta(0) + regressoes.get(1).beta(1) * prices.get(cont).getMemory()
								+ regressoes.get(1).beta(2) * prices.get(cont).getvCPU()
						: 5);

		// System.out.println(prices.get(cont).getvCPU()+" "+cpuTemp[cont]);

		memoryTemp[cont] = 100 - (
				regressoes.get(2).beta(0) + regressoes.get(2).beta(1) * Math.log10(prices.get(cont).getMemory())
						+ regressoes.get(2).beta(2) * Math.log10(prices.get(cont).getvCPU()) > 0
								?  regressoes.get(2).beta(0)
												+ regressoes.get(2).beta(1) * Math.log10(prices.get(cont).getMemory())
												+ regressoes.get(2).beta(2) * Math.log10(prices.get(cont).getvCPU())
								: 0.00001);
		
		
		/*System.out.println(time <= timeTemp[cont]);
		System.out.println((CPUusage <= cpuTemp[cont])+" "+CPUusage+" "+cpuTemp[cont]);
		System.out.println((Menusage <= memoryTemp[cont]) +" "+Menusage+" "+memoryTemp[cont]);
		System.out.println(((timeTemp[cont] ) <= custoTemp[cont])+" "+(timeTemp[cont] * time)+" "+custoTemp[cont]);*/
		
		if (time <= timeTemp[cont] && CPUusage <= cpuTemp[cont] && Menusage <= memoryTemp[cont]
				//&& (timeTemp[cont] ) <= custoTemp[cont]
				) {

			System.out.println("CONTROLE "+custoTemp[cont]);
			custoTemp[cont] = timeTemp[cont] * alfaT + pricesTemp[cont] * alfaC
					+ (cpuTemp[cont] + memoryTemp[cont]) * alfaD;
			
		} else {
			custoTemp[cont] = Double.MAX_VALUE;
		}
		
		//System.out.println("custoTemp[cont] "+custoTemp[cont]);

		return custoTemp[cont];
	}

}