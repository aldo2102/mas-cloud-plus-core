#!/bin/bash


controle=1
executador=1
for k in 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61 62 63 64 65 66 67 68 69 70 71 72 73 74 75 76 77 78 79 80 81 82 83 84 85 86 87 88 89 90 91 92 93 94 95 96 97 98 99 100 ;
do
	echo " --------------- Started $k --------------------- "
	
        pkill -9 -f vagrant
        pkill -9 -f ruby
        pkill -9 -f 'java -jar'
        cd 'mas-cloud folder'
        while [[ $executador -eq 1 ]]
				 do
				
					while IFS= read -r line4
					do
					
						if [[ $line4 == *"An action 'read_state' was attempted on the machine 'default',"* ]];
						then
							pkill -9 -f vagrant
							pkill -9 -f ruby
							pkill -9 -f 'java -jar'
							executador=1
						else
						
							executador=0
							break
						fi
						
					done  < <(stdbuf -oL vagrant destroy -f)
				 done 
        
        
        
        pkill -9 -f 'java -jar'
        #cd 'aws'
        
        stdbuf -oL java -jar mascloud.jar 1 "" 3 3 100 100 100 dummy aldohenrique2 2 |
        while IFS= read -r line
        do
                echo "1 $line"
                if [[ $line == *"/JADE terminating."* ]];
                     then
                     echo "Finalize JADE"
                     executador=3
                     break
                fi
                if [ ${#line} -le 23 ]
                then
                        if [[ $line == *"Tamanho 12"* ]];
                        then
                        	executador=1
                               while [[ $executador -eq 1 ]]
				 do
				
					while IFS= read -r line3
					do
					
						if [[ $line3 == *"An action 'read_state' was attempted on the machine 'default',"* ]];
						then
							executador=1
							
						else
							executador=0
							break
						fi
						
					done  < <(stdbuf -oL  vagrant destroy -f)
				 done
                        fi
                        
                        if [[ $line == *"Tamanho 13"* ]];
                        then
                            	cd aldohenrique2
                                :
                                executador=1
                                valor=$((1 + $RANDOM % 10))
                                echo $valor
                                
                                sleep 5
                                while [[ $executador -eq 1 ]]
                                        do
                                        	
                                        	while IFS= read -r line2
        					do
        						echo "$valor $line2"
                                            		if [[ $line2 == *"An action 'read_state' was attempted on the machine 'default',"* ]];
                                            		then
                                            		  
                                			  executador=1
                                            		fi
                                            		
                                            		let executador=0
                                            		#Select and run the application
                                            	done < <(stdbuf -oL vagrant ssh -- -t 'source  /home/ubuntu/.profile ; rm /home/ubuntu/hdata/* -r ;  rm /home/ubuntu/hdfs_data/* -r ; rm /home/ubuntu/metadata/* -r ; ssh-keygen -R localhost ; sudo rm -r /tmp/* ; sudo service ssh restart ; printf "\ny" | ssh-keygen -t rsa -P '' ; cat /home/ubuntu/.ssh/id_rsa.pub >> /home/ubuntu/.ssh/authorized_keys ; printf "y" | hdfs namenode -format ;  printf "y" | hadoop namenode -format ; printf "Y" | /home/ubuntu/hadoop-2.7.1/bin/hadoop namenode -format ; ssh-keyscan localhost >> /home/ubuntu/.ssh/known_hosts ; printf "yes\nyes\nyes" | /home/ubuntu/hadoop-2.7.1/sbin/stop-all.sh ; hdfs dfsadmin -safemode get ;  hdfs dfsadmin -safemode enter ;  hdfs dfsadmin -safemode leave ;  printf "yes\nyes" |  hadoop namenode -format ; printf "yes\nyes" | /home/ubuntu/hadoop-2.7.1/sbin/start-all.sh ; cd /home/ubuntu/BigDataBench_V5.0_BigData_MicroBenchmark-main/Hadoop/wordcount ; ./genData-wc.sh ' $valor ' ; ./run-wordcount.sh ' $valor)
                                      #Sort ; ./genData-sort.sh ' $valor ' ; ./run-terasort.sh ' $valor
                                      #PageRank ; ./genData-pagerank.sh ' $valor ' ; ./run-PageRank.sh ' $valor
                                      #wordcount ; ./genData-wc.sh ' $valor ' ; ./run-wordcount.sh ' $valor
                                      #f 1-6 (forCPU cores)
                                      #1-10 (for memory in GB)
                                      #1-8 (for total executors)
                                            	
                                	if [[ $executador -eq 3 ]]  ; then
                                		break
                                	fi
							          	
                                done
                                
                                executador=1
                                
                contadorExit=0
                while [[ $executador -eq 1 ]]
                echo "Executor $executador  $contadorExit "
				 do
                    if [[ $contadorExit -eq 1 ]] ;
                    then
                        
                        break
                    fi
                
				    
					while IFS= read -r line3
					do
						if [[ $line3 == *'==> default: Instance is not created. Please run `vagrant up` first.'* ]];
						then
							echo "Closed Vagrant"
							executador=3
                            contadorExit=$((contadorExit+1))
						fi
						if [[ $line3 == *"==> default: Terminating the instance..."* ]];
						then
							echo "Quitting Vagrant"
                            cd '/home/aldohenrique/Dropbox/Area de Trabalho/Minhas Aulas/UnB/Doutorado/pesquisa/Comparações/aws/aldohenrique2'
                            vagrant destroy -f 

                            contadorExit=$((contadorExit+1))
                            pkill -9 -f vagrant
							pkill -9 -f ruby
							executador=1
							continue
						fi
						if [[ $line3 == *"An action 'read_state' was attempted on the machine 'default',"* ]];
						then
							executador=1
							
						    	pkill -9 -f vagrant
							pkill -9 -f ruby
						else
							executador=0
							break
						fi
						echo "Ending $executador"
					done < <(stdbuf -oL  vagrant destroy -f )
				 done
				 if [[ $executador -eq 3 ]] ;
						then
						break
				 fi		
				 
                                #cd '/home/aldohenrique/Dropbox/Area de Trabalho/Minhas Aulas/UnB/Doutorado/pesquisa/Comparações/aws'
                                :

                                
                                rm MTPs-Main-Container* ;
                                controle=2
                                sleep 20
                                pkill -9 -f vagrant
                                pkill -9 -f ruby
                                pkill -9 -f 'java -jar'
                                vagrant destroy -f 
                                break
                                
                        fi
                fi
        done 
  done

pkill -9 -f vagrant
pkill -9 -f ruby
pkill -9 -f 'java -jar'
vagrant destroy -f 

