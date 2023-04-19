# -*- coding: utf-8 -*-
from ortools.linear_solver import pywraplp
import numpy as np
import sys
import csv
import requests
import json
from operator import is_not
from functools import partial
from more_itertools import locate

quantidadeProvedores2 = 1

takeClosest = lambda num, collection: min(collection,
                                          key=lambda x: abs(x - num))

responseAws = requests.get(
    "https://aws.amazon.com/ec2/pricing/json/linux-od.json")

responseAzure = open('azureprice-export.json', )

responseGoogle = open('priceCustom.json', )

aws = False
azure = False
google = True

# returns JSON object as
# a dictionary
dataAzure = json.load(responseAzure)
dataGoogle = json.load(responseGoogle)
# Iterating through the json
# list

my_json_string = "[ " + responseAws.text + " ]"

my_json_string = my_json_string

to_python = json.loads(my_json_string)
to_python = to_python[0]['config']['regions']
maquinas = []
i = 0
y = 0

instancias = 0
#Contador da AWS
if (aws == True):
    for regions in to_python:
        #region[i][0]= regions["region"]
        for instanceTypes in regions["instanceTypes"]:
            for sizes in instanceTypes["sizes"]:
                instancias = instancias + 1

#Contador da Azure

if (azure == True):
    for linhas in dataAzure:
        instancias = instancias + 1

if (google == True):
    for linhas in dataGoogle['gcp_price_list'].keys():
        instancias = instancias + 1
print(instancias)
provedor = np.zeros(instancias)
region = np.random.uniform(1, 999, (6, 6))
instancias2 = 0
quantidadeIntancias = instancias

mv = np.empty(quantidadeIntancias, dtype=object)

#Valores da AWS
if (aws == True):
    for regions in to_python:
        #region[i][0]= regions["region"]
        for instanceTypes in regions["instanceTypes"]:
            for sizes in instanceTypes["sizes"]:
                region[2][i] = sizes["vCPU"]
                region[3][i] = sizes["memoryGiB"]
                region[4][i] = sizes["valueColumns"][0]["prices"]["USD"]
                region[5][i] = 1
                provedor[instancias2] = 1
                mv[i] = sizes["size"]
                i = i + 1
                instancias2 = instancias2 + 1

#Valores da azure
if (azure == True):
    for linhas in dataAzure:
        region[2][i] = float(linhas["numberOfCores"])
        region[3][i] = float(linhas["memoryInMB"] / 1000)
        region[4][i] = float(linhas["linuxPrice"])
        region[5][i] = 2
        mv[i] = linhas["name"]

        i = i + 1
        provedor[instancias2] = 2
        instancias2 = instancias2 + 1
        #print(linhas["numberOfCores"]," ",linhas["memoryInMB"]/1000," ",linhas["linuxPrice"])

#Valores da azure
if (google == True):
    print(str(dataGoogle['gcp_price_list']))
    for linhas in dataGoogle['gcp_price_list'].keys():
        print(dataGoogle['gcp_price_list'][linhas])
        if (linhas == 'e2-standard-8'):
            region[2][i] = float(8)
            region[3][i] = float(32768 / 1000)
            region[4][i] = 0.48
        if (linhas == 'e2-standard-4'):
            region[2][i] = float(4)
            region[3][i] = float(16384 / 1000)
            region[4][i] = 0.24
        if (linhas == 'custom-12-49152'):
            region[2][i] = float(12)
            region[3][i] = float(49152 / 1000)
            region[4][i] = 0.72
        region[5][i] = 3
        mv[i] = linhas
        print(mv)
        i = i + 1
        provedor[instancias2] = 3
        instancias2 = instancias2 + 1
        #print(linhas["numberOfCores"]," ",linhas["memoryInMB"]/1000," ",linhas["linuxPrice"])

print("instancias2", instancias2, provedor)


def mi2ai(n, m, line_length):
    return n * M + m


def ai2mi(index, line_length):
    return [(index // line_length), index % line_length]


# Parametros

#uC = 0.01
#uT = 1- uC

p = 1
#c_n = [1,2,4,8,16,32,64,128,256,512]

ClsC = 164  #Tem de ser um numero na base 2
ClsM = 1024

flag = 1  #prioridade 0 - Custo*Desperdicio e 1 - Tempo
#Cls = ClsMemory

arguments = sys.argv[1:]
count = len(arguments)

alphaT = 0.9
alphaC = 0.1
alphaD = 0.1

print(count)
if (count > 0):
    print(arguments[0])
    print(arguments[1])
    arquivo = arguments[0]
    alphaT = float(arguments[1])
    alphaC = float(arguments[2])
    alphaD = float(arguments[3])

else:
    arquivo = 'wordCount.csv'

print("Alfas ", alphaT, " ", alphaC, " ", alphaD)

num_lines = 0
with open(arquivo) as csvfile:
    spamreader = csv.reader(csvfile, delimiter=',', lineterminator='\n')
    for row in spamreader:
        try:
            float(row[2])
            num_lines = num_lines + 1
        except ValueError:
            print()
#num_lines = sum(1 for line in open(arquivo))
#print(num_lines)
c_n = np.random.rand(num_lines) * 2
m_n = np.random.rand(num_lines) * 2
N = len(c_n)

QuantiUsuario = 1  #Tem de ser MENOR que CLS

M = QuantiUsuario

T_n_m = np.zeros((M, N))

D_n_m = np.zeros((M, N))

n = 0
mediaT = float(0.00)
mediaD = float(0.00)
print()

minT = float(sys.float_info.max)
minD = float(sys.float_info.max)

with open(arquivo) as csvfile:
    spamreader = csv.reader(csvfile, delimiter=',', lineterminator='\n')
    for row in spamreader:
        try:

            memoryTeste = int(float(row[5]) / 1000000)
            memoryTeste = float(100 -
                                (((float(row[4]) / 1000) * 100) / memoryTeste))
            #print(memoryTeste)
            if (memoryTeste > 70):
                float(row[2])
                #CPU no USED, Agents, vCPU, CPU USED, Time
                c_n[n] = row[2]  #cpu
                m_n[n] = int(float(row[5]) / 1000000)  #memory

                T_n_m[0][n] = float(row[10])  #tempo
                T_n_m[0][N - n - 1] = float(row[10])  #tempo

                Dmemory = 100 - (((float(row[4]) / 1000) * 100) / m_n[n])

                D_n_m[0][n] = (float(row[0]) + Dmemory
                               )  # 0 Uso de CPU e 5 Memoria
                D_n_m[0][N - n - 1] = (float(row[0]) + Dmemory
                                       )  # 0 Uso de CPU e 5 Memoria

                #print("Teste55 ",T_n_m[0][n])
                #print("Teste56 ",D_n_m[0][n])
            else:
                T_n_m[0][n] = sys.maxint
                T_n_m[0][N - n - 1] = sys.maxint
                D_n_m[0][n] = sys.maxint
                D_n_m[0][N - n - 1] = sys.maxint
                c_n[n] = sys.maxint

            n = n + 1
            print()
            print("----- ", T_n_m[0][n], " ", D_n_m[0][n])
            print()

            #T_n_m[0][n] = T_n_m[0][n] * alphaT
            #D_n_m[0][n] = D_n_m[0][n] * alphaD

            print()
            print("----- ", T_n_m[0][n], " ", D_n_m[0][n])
            print()

        except:
            n = n
p = 0.05

c_n2 = list(dict.fromkeys(c_n))
m_n2 = np.zeros((instancias2, len(c_n2) * quantidadeIntancias))
provedorEsc = np.empty(len(c_n2) * quantidadeIntancias, dtype=object)
teste4 = np.zeros(len(c_n2) * quantidadeIntancias)

ii = 0
for m in range(0, quantidadeIntancias - 2):
    for n in range(0, len(c_n2)):
        if (int(float(region[2][m])) == int(float(c_n2[n]))
                and int(region[5][m]) == 1):
            m_n2[m][ii] = float(region[3][m])
            #print(" -1 ",m_n2[m][ii])
            teste4[ii] = c_n2[n]
            ii = ii + 1

for m in range(0, quantidadeIntancias - 2):
    for n in range(0, len(c_n2)):
        if (int(float(region[2][m])) == int(float(c_n2[n]))
                and int(region[5][m]) == 2):
            m_n2[m][ii] = float(region[3][m])
            teste4[ii] = c_n2[n]
            ii = ii + 1

for m in range(0, quantidadeIntancias):
    for n in range(0, len(c_n2)):
        print(float(region[2][m]), c_n2[n], region[5][m])
        if (int(float(region[2][m])) == int(float(c_n2[n]))):

            m_n2[m][ii] = float(region[3][m])
            teste4[ii] = c_n2[n]
            ii = ii + 1
#mvI = list(dict.fromkeys(mvI))
print("ii ", ii)
print(teste4)

print(m_n2)
i = 0

mvI = np.empty((quantidadeProvedores2, ii), dtype=object)
print("mvI", mvI, ii, quantidadeProvedores2)
b_m = np.zeros(num_lines)
provedorPreco = np.zeros(quantidadeIntancias)
#print(len(c_n2))
tert = 0
n = 0
ii = 0
#print("Teste1 ",num_lines," ",len(region[5])," ",len(c_n2))

#Calculos para AWS

if (aws == True):
    for n in range(0, len(c_n)):
        controleCPU = 0
        for m in range(0, len(region[2])):
            if (int(region[5][m]) == 1 and ii < N):

                cpus = list(locate(region[2], lambda x: x == c_n[n]))

                #print(c_n[n]," ",ii," ",controleCPU," ",cpus)
                if (controleCPU in cpus):
                    men = 0
                    memorias = []
                    for o in range(0, len(region[3])):
                        if (region[2][o] == c_n[n]):
                            memorias.append(region[3][o])
                    #print(memorias)

                    if (memorias != []):
                        men = min(enumerate(memorias),
                                  key=lambda x: abs(x[1] - (float(m_n[n]))))
                        #print(men)
                        #print(region[2][m]," ", c_n[n]," ", int(region[3][m])," ",men," ",int(m_n[n]))

                        if (int(float(region[2][m])) == int(float(c_n[n]))
                                and (int(region[3][m]) + 1 == int(men[1])
                                     or int(region[3][m]) - 1 == int(men[1])
                                     or int(region[3][m]) == int(men[1]))):

                            #print("Teste3 ",c_n[n]," ",c_n2[n]," ",men[1]," ",region[3][m])
                            try:
                                print(region[4][m], " ", T_n_m[0][n])
                                b_m[ii] = (region[4][m] * T_n_m[0][n])
                                print(b_m[ii])
                                provedorPreco[n] = provedor[m]
                                mvI[0][ii] = mv[m]
                                print("-- ", mv[m], " ", region[2][m])
                                ii = ii + 1
                                break
                            except ValueError:
                                print(ValueError)
                                t = 0

                controleCPU = controleCPU + 1
print("----------")
ii = 0

#Calculos para Azure
if (azure == True):
    for n in range(0, len(c_n)):
        controleCPU = 0
        for m in range(0, len(region[2])):
            if (int(region[5][m]) == 2 and ii < N):

                cpus = list(locate(region[2], lambda x: x == c_n[n]))

                #print(c_n[n]," ",ii," ",controleCPU," ",cpus)
                if (controleCPU in cpus):
                    men = 0
                    memorias = []
                    for o in range(0, len(region[3])):
                        if (region[2][o] == c_n[n]):
                            memorias.append(region[3][o])
                    #print(memorias)

                    if (memorias != []):
                        men = min(enumerate(memorias),
                                  key=lambda x: abs(x[1] - (float(m_n[n]))))
                        #print(men)
                        #print(region[2][m]," ", c_n[n]," ", int(region[3][m])," ",men," ",int(m_n[n]))

                        if (int(float(region[2][m])) == int(float(c_n[n]))
                                and (int(region[3][m]) + 1 == int(men[1])
                                     or int(region[3][m]) - 1 == int(men[1])
                                     or int(region[3][m]) == int(men[1]))):

                            #print("Teste3 ",c_n[n]," ",c_n2[n]," ",men[1]," ",region[3][m])
                            try:
                                #print(region[4][m]," ",T_n_m[0][n])
                                b_m[ii] = (region[4][m] * T_n_m[0][n])

                                provedorPreco[n] = provedor[m]
                                mvI[1][ii] = mv[m]
                                #print("-- ",mv[m]," ",region[2][m])
                                ii = ii + 1
                                break
                            except ValueError:
                                print(ValueError)
                                t = 0

                controleCPU = controleCPU + 1

#Calculos para Google
ii = 0
if (google == True):
    for n in range(0, len(c_n)):
        controleCPU = 0
        for m in range(0, len(region[2])):
            if (int(region[5][m]) == 3 and ii < N):

                    men = 0
                    memorias = []
                    for o in range(0, len(region[3])):

                        if (region[2][o] == c_n[n]):
                            memorias.append(region[3][o])
                            #print(memorias)

                            #print("Teste3 ",c_n[n]," ",c_n2[n]," ",men[1]," ",region[3][m])
                            try:
                                #print(region[4][m]," ",T_n_m[0][n])
                                b_m[ii] = (region[4][m] * T_n_m[0][n])
                                if (n < len(provedorPreco)):
                                    print(provedorPreco, n, provedor, m, mv)
                                    provedorPreco[n] = provedor[m]
                                print(len(mvI) - 1, mvI, ii)
                                mvI[0] = mv
                                #print("-- ",mv[m]," ",region[2][m])
                                ii = ii + 1
                                break
                            except ValueError:
                                print(ValueError)
                                t = 0

                    controleCPU = controleCPU + 1

#exit(1)
print(mvI[0])
print("teste ", len(provedorPreco))

A_n_m = np.random.uniform(1, 1, (M, N))

C_n_m = np.zeros((M, N))

minC = float(sys.float_info.max)
mediaC = float(0.00)
for m in range(0, M):
    for n in range(0, N):
        if b_m[n] > 0:
            C_n_m[m, n] = b_m[n]
        else:
            C_n_m[m, n] = sys.maxsize + 0.0
        #print("t ",b_m[n])
        #C_n_m[m, n] = C_n_m[m, n]*alphaC

#print(b_m)

print()

alphaT = 0.9
alphaC = 0.00
alphaD = 0.00

n = 0
for m in range(0, M):
    for n in range(0, N):
        #D_n_m[m, n] = (D_n_m[m, n]*alphaD)
        #C_n_m[m, n] = (C_n_m[m, n]*alphaC)
        #T_n_m[m, n] = (T_n_m[m, n]*alphaT)/1000
        mediaT = T_n_m[m, n] + mediaT  #
        mediaD = D_n_m[m, n] + mediaD
        mediaC = C_n_m[m, n] + mediaC
        if (minT > T_n_m[0][n]):
            minT = T_n_m[0][n]
        if (minD > D_n_m[0][n]):
            minD = D_n_m[0][n]
        if (minC > C_n_m[0][n]):
            minC = C_n_m[0][n]
        #print(minD)
        n = n + 1
        #print(c_n[n])

print("Matriz Desperdicio")
strmatrix = ''
for m in range(0, M):
    strmatrix += '[ '
    for n in range(0, N):
        strmatrix += str("{:.2f}".format(D_n_m[m, n])) + " "
    strmatrix += ']\n'
print(strmatrix)

print("Matriz Tempo")
strmatrix = ''
for m in range(0, M):
    strmatrix += '[ '
    for n in range(0, N):
        strmatrix += str("{:.2f}".format(T_n_m[m, n])) + " "
    strmatrix += ']\n'
print(strmatrix)

print("Matriz Custo")
strmatrix = ''
for m in range(0, M):
    strmatrix += '[ '
    for n in range(0, N):
        strmatrix += str("{:.2f}".format(C_n_m[m, n])) + " "
    strmatrix += ']\n'
print(strmatrix)

# # Variaveis de Decisï¿½es

#

cv = lambda x: np.std(x, ddof=1) / np.mean(x) * 100
TCoV = cv(T_n_m[0])
DCoV = cv(D_n_m[0])
CCoV = cv(C_n_m[0])

#T = ((float(minT)+np.std(T_n_m[0])) + ((mediaT/n) * alphaT))
T = max(((mediaT / n) * (alphaT)), float(minT))
#T = 999999999999999999
print("Tempo ", ((mediaT / n) * (1 - alphaT)), " ", minT, " ", T)

#C = ((float(minC)+np.std(C_n_m[0])) + ((mediaC/n) * alphaC))
C = max(((mediaC / n) * (alphaC)), (float(minC)))
#C = 99999999999999
print("Custo ", (mediaC / n) * (1 - alphaC), " ", float(minC), " ", C)

#D = ((float(minD)+np.std(D_n_m[0])) + ((mediaD/n) * alphaD))
D = max(((mediaD / n) * (alphaD)), (float(minD)))
#D =  999999999999999999
print("Desperdício ", (mediaD / n) * (1 - alphaD), " ", float(minD), " ", D)

TCoV = T
DCoV = D

np.std(T_n_m[0])

print()
print("Tempo médio " + str(TCoV))
print("Custo médio " + str(C))
print("Desperdicio médio " + str(D) + " " + str(np.std(D_n_m[0])) + " " +
      str(cv(D_n_m[0])))

contadoresTeste = 0
while True:
    solver = pywraplp.Solver('LinearProgrammingExample',
                             pywraplp.Solver.GLOP_LINEAR_PROGRAMMING)
    X_n_m = []
    for n in range(0, N):
        for m in range(0, M):
            X_n_m.append(
                solver.NumVar(0, 1, "X[" + str(n) + "," + str(m) + "]"))

    Tp_n_m = []
    for n in range(0, N):
        for m in range(0, M):
            Tp_n_m.append(
                solver.NumVar(0, solver.infinity(),
                              "Tp[" + str(n) + "," + str(m) + "]"))

    Cp_n_m = []
    for n in range(0, N):
        for m in range(0, M):
            Cp_n_m.append(
                solver.NumVar(0, solver.infinity(),
                              "Cp[" + str(n) + "," + str(m) + "]"))

    Dp_n_m = []
    for n in range(0, N):
        for m in range(0, M):
            Dp_n_m.append(
                solver.NumVar(0, solver.infinity(),
                              "dp[" + str(n) + "," + str(m) + "]"))

    solver.NumVariables()
    # %%
    head = 0
    '''ct = solver.Constraint(1, ClsC, str(head))
    for n in range(0, M):
        head += 1
        for m in range(0, N):
          ct.SetCoefficient(X_n_m[mi2ai(n, m, N)], c_n[m])

    ct = solver.Constraint(1, ClsM, str(head))
    for n in range(0, M):
        head += 1
        for m in range(0, N):
          ct.SetCoefficient(X_n_m[mi2ai(n, m, N)], m_n[m])'''

    for n in range(0, M):
        const1 = solver.Constraint(1, 1, str(head))
        head += 1
        for m in range(0, N):
            const1.SetCoefficient(X_n_m[mi2ai(n, m, N)], 1)

    for n in range(0, M):
        const2 = solver.Constraint(-solver.infinity(), TCoV, str(head))
        for m in range(0, N):
            head += 1
            const2.SetCoefficient(X_n_m[mi2ai(n, m, N)], T_n_m[n, m])

    for n in range(0, M):
        const3 = solver.Constraint(0, C, str(head))
        for m in range(0, N):
            head += 1
            const3.SetCoefficient(X_n_m[mi2ai(n, m, N)], C_n_m[n, m])

    for n in range(0, M):
        const4 = solver.Constraint(0, DCoV, str(head))
        for m in range(0, N):
            head += 1
            const4.SetCoefficient(X_n_m[mi2ai(n, m, N)], D_n_m[n, m])

    # # Solver

    objective = solver.Objective()

    for n in range(0, M):
        for m in range(0, N):
            objective.SetCoefficient(X_n_m[mi2ai(n, m, N)], D_n_m[n, m])
            objective.SetCoefficient(X_n_m[mi2ai(n, m, N)], T_n_m[n, m])
            objective.SetCoefficient(X_n_m[mi2ai(n, m, N)], C_n_m[n, m])

    objective.SetMinimization()

    status = solver.Solve()

    #Imprimiremos a solucao
    if status == pywraplp.Solver.OPTIMAL:
        #print('Solution:')
        break
        #print('Objective value =', solver.Objective().Value())
    else:
        print('Objective value =', solver.Objective().Value())
        if alphaT < alphaC and alphaT < alphaD:
            C = C + np.std(C_n_m[0])
            print("Custo ", float(minC), " ", C)
            D = D + np.std(D_n_m[0])
            print("Despercicio ", float(minD), " ", D)

        elif alphaC < alphaT and alphaC < alphaD:
            T = T + np.std(T_n_m[0])
            print("Tempo ", float(minT), " ", T)
            D = D + np.std(D_n_m[0])
            print("Despercicio ", float(minD), " ", D)

        elif alphaD < alphaT and alphaD < alphaC:
            T = T + np.std(T_n_m[0])
            print("Tempo ", float(minT), " ", T)
            C = C + np.std(C_n_m[0])
            print("Custo ", float(minC), " ", C)
        else:
            T = T + np.std(T_n_m[0])
            print("Tempo ", float(minT), " ", T)
            C = C + np.std(C_n_m[0])
            print("Custo ", float(minC), " ", C)
            D = D + np.std(D_n_m[0])
            print("Despercicio ", float(minD), " ", D)

        TCoV = T
        DCoV = D

############ fim while
#exit()
strmatrix = ''
strmatrix += '[ '
for n in range(0, N):
    strmatrix += str(int(c_n[n])) + " "
strmatrix += ']\n\n'
#print(strmatrix)

strmatrix = ''
strmatrix += '[ '
for n in range(0, N):
    strmatrix += str(int(m_n[n])) + " "
strmatrix += ']\n\n'
#print(strmatrix)

strmatrix = ''
for n in range(0, M):
    strmatrix += '[ '
    for m in range(0, N):
        strmatrix += str("{:.1f}".format(X_n_m[mi2ai(
            n, m, N)].solution_value())) + " "

    strmatrix += ']\n'
print(strmatrix)
print(c_n)

#print(strmatrix)
#print(M)
#print(N)

#cont= sys.float_info.max
cont = 0
sM = 0
sN = 0
for n in range(0, M):
    for m in range(0, N):
        if (float(X_n_m[mi2ai(n, m, N)].solution_value()) > 0):

            print("teste ", (
                (float(X_n_m[mi2ai(n, m, N)].solution_value()) * D_n_m[n, m]) +
                (float(X_n_m[mi2ai(n, m, N)].solution_value()) * T_n_m[n, m]) +
                (float(X_n_m[mi2ai(n, m, N)].solution_value()) * C_n_m[n, m])))
            if ((
                (float(X_n_m[mi2ai(n, m, N)].solution_value()) * D_n_m[n, m]) +
                (float(X_n_m[mi2ai(n, m, N)].solution_value()) * T_n_m[n, m]) +
                (float(X_n_m[mi2ai(n, m, N)].solution_value()) * C_n_m[n, m]))
                    >= cont):
                cont = ((float(X_n_m[mi2ai(n, m, N)].solution_value()) *
                         D_n_m[n, m]) +
                        (float(X_n_m[mi2ai(n, m, N)].solution_value()) *
                         T_n_m[n, m]) +
                        (float(X_n_m[mi2ai(n, m, N)].solution_value()) *
                         C_n_m[n, m]))
                sM = m
                sN = n

print(X_n_m[mi2ai(sN, sM, N)].solution_value())
print("Será alocado ", c_n[sM])
print()
if (X_n_m[mi2ai(sN, sM, N)].solution_value() < 1):
    print("1")
    print(sN, " ", sM)
    print(
        "Tempo ", T_n_m[sN, sM] +
        T_n_m[sN, sM] * X_n_m[mi2ai(sN, sM, N)].solution_value())
    print(
        "Custo ", C_n_m[sN, sM] +
        C_n_m[sN, sM] * X_n_m[mi2ai(sN, sM, N)].solution_value())
    print(
        "Desperdicio ", D_n_m[sN, sM] +
        D_n_m[sN, sM] * X_n_m[mi2ai(sN, sM, N)].solution_value())
    print()
    #print("Posição Matriz resultado ", m)
    #print("Recursos ", Cls)
    #print("Prioridade %s" % (sys.argv[1]))

else:
    print("2")
    print("Tempo ", T_n_m[sN, sM])
    print("Custo ", C_n_m[sN, sM])
    print("Desperdicio ", D_n_m[sN, sM])
    print()
    #print("Posição Matriz resultado ", m)
    #print("Recursos ", Cls)
    #print("Prioridade %s" % (sys.argv[1]))


#x = int(provedorPreco[sM])
print("1 - AWS, 2 - Azure ")
#print("Provedor ", int(provedorPreco[sM]))
print("vCPU ", str(c_n[sM]))
print("Memoria ", str(m_n[sM]))
'''for n in range(0, 2):
    for m in range(0, len(mvI[n])):
      print(mvI[n][m])'''
#print(mvI, x - 1, sM)
#print("Instancia ", str(sN))
x=np.where(teste4 == c_n[sM])
print(x[0][0])
print("Instancia ", str(mvI[0][x].value))

