#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include<unistd.h>
#include<sys/types.h>
#include<sys/stat.h>
#include<sys/socket.h>
#include<arpa/inet.h>
#include<netdb.h>
#include<signal.h>
#include<fcntl.h>

#include <wiringPi.h>
#include "softServo.h"

#include <stdio.h>
#include <stdlib.h>
#define RCSERVO 23

#include <time.h>

#define CONNMAX 1000
#define BYTES 1024

int listenfd, clients[CONNMAX];
void error(char *);
void startServer(char *);
void respond(int);

int main()
{
    struct sockaddr_in clientaddr;
    socklen_t addrlen;
    char c;    
    char PORT[6]="80";

    int slot=0;

    // Setting all elements to -1: signifies there is no client connected
    int i;
    for (i=0; i<CONNMAX; i++)
        clients[i]=-1;
    startServer(PORT);

    // ACCEPT connections
    while (1)
    {
        addrlen = sizeof(clientaddr);
        clients[slot] = accept (listenfd, (struct sockaddr *) &clientaddr, &addrlen);

        if (clients[slot]<0)
            error ("accept() error");
        else
        {
            if ( fork()==0 )
            {
                respond(slot);
                exit(0);
            }
        }

        while (clients[slot]!=-1) slot = (slot+1)%CONNMAX;
    }

    return 0;
}

//start server
void startServer(char *port)
{
    struct addrinfo hints, *res, *p;

    // getaddrinfo for host
    memset (&hints, 0, sizeof(hints));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE;
    if (getaddrinfo( NULL, port, &hints, &res) != 0)
    {
        perror ("getaddrinfo() error");
        exit(1);
    }
    // socket and bind
    for (p = res; p!=NULL; p=p->ai_next)
    {
        listenfd = socket (p->ai_family, p->ai_socktype, 0);
        if (listenfd == -1) continue;
        if (bind(listenfd, p->ai_addr, p->ai_addrlen) == 0) break;
    }
    if (p==NULL)
    {
        perror ("socket() or bind()");
        exit(1);
    }

    freeaddrinfo(res);

    // listen for incoming connections
    if ( listen (listenfd, 1000000) != 0 )
    {
        perror("listen() error");
        exit(1);
    }
}

//client connection
void respond(int n)
{
	static struct timespec OpenTime,CloseTime;
    char mesg[99999], *reqline[99999];
    int rcvd, fd, bytes_read;   
	int degree=0;
    memset( (void*)mesg, (int)'\0', 99999 );

    rcvd=recv(clients[n], mesg, 99999, 0);

    if (rcvd<0)    // receive error
        fprintf(stderr,("recv() error\n"));
    else if (rcvd==0)    // receive socket closed
        fprintf(stderr,"Client disconnected upexpectedly.\n");
    else    // message received
    {
		
		printf("client = %d\n",n);
        printf("mesg : %s\n", mesg);
        printf("----------\n");
	
		//reqline[0] = strtok (mesg, " \t\n");
       

        // if ( strncmp(reqline[0], "command=1", 4)==0 )
		if( strncmp(mesg,"command=1", 9)==0 )
        {
			
				 
				    if(wiringPiSetupGpio () == -1) 
					{
						printf("servoMotor error\n", mesg);
					    write(clients[n], "-1\n", 1);	
					}
					else 
					{
						 write(clients[n], "1\n", 1);
						 printf("Door is open!!\n");
						 clock_gettime(CLOCK_REALTIME,&OpenTime);
						 printf("open Time : %d.%d\n",OpenTime.tv_sec,OpenTime.tv_nsec);

						softServoSetup(RCSERVO,-1,-1,-1,-1,-1,-1,-1);
						
						softServoWrite(RCSERVO,-250);
						delay(1000);
						softServoWrite(RCSERVO,-250);
						
						
						
					
					}



		}
		else if( strncmp(mesg,"command=0", 9)==0 )
		{
			
				
				   if(wiringPiSetupGpio () == -1)
					 {
					   printf("servoMotor error\n", mesg);
						write(clients[n], "-1\n", 1);
					 }
					else 
					{
						 write(clients[n], "0\n", 1);
						 printf("Door is close!!\n");
						 clock_gettime(CLOCK_REALTIME,&CloseTime);
						 printf("close Time : %d.%d\n",CloseTime.tv_sec,CloseTime.tv_nsec);
				

						softServoSetup(RCSERVO,-1,-1,-1,-1,-1,-1,-1);
						
						softServoWrite(RCSERVO,1250);
						delay(1000);
						softServoWrite(RCSERVO,1250);
					
					}
		}
		else 
			{ 
			write(clients[n], "Please send correct command\n", 21);
			}

   
    //Closing SOCKET
	}
    shutdown (clients[n], SHUT_RDWR);         //All further send and recieve operations are DISABLED...
    close(clients[n]);
    clients[n]=-1;
}

		
			/*
            reqline[1] = strtok (NULL, " \t");
            reqline[2] = strtok (NULL, " \t\n");
            printf("%s\n",reqline[0]);
            printf("%s\n",reqline[1]);
		    printf("%s\n",reqline[2]);
			
			write(clients[n], "Please send message\n", 21);
            if ( strncmp( reqline[2], "HTTP/1.0", 8)!=0 && strncmp( reqline[2], "HTTP/1.1", 8)!=0 )
            {   	
                write(clients[n], "400 Bad Request\n", 25);
            }
            else
            {
                if ( strncmp(reqline[1], "/\0", 2)==0 ) write(clients[n], "Please send message\n", 21);
                else if( strncmp(reqline[1], "/?command=1", 11) == 0 )
				{
				   //servomotor
				   
					if(wiringPiSetupGpio () == -1) printf("servoMotor error\n", mesg);
					else 
					{
						softServoSetup(RCSERVO,-1,-1,-1,-1,-1,-1,-1);
						
						softServoWrite(RCSERVO,-250);
						delay(1000);
						softServoWrite(RCSERVO,-250);

						//send message open (== 1)
						clock_gettime(CLOCK_REALTIME,&OpenTime);
						 write(clients[n], "1\r\n", 7); 
						printf("open Time : %d.%d\n",OpenTime.tv_sec,OpenTime.tv_nsec);
						
					}
				}
                  else if( strncmp(reqline[1], "/?command=0", 11)==0 )
					{

                      	if(wiringPiSetupGpio () == -1) printf("servoMotor error\n", mesg);
						else 
						{
							softServoSetup(RCSERVO,-1,-1,-1,-1,-1,-1,-1);
							
							softServoWrite(RCSERVO,1250);

							delay(1000);
							softServoWrite(RCSERVO,1250);

							clock_gettime(CLOCK_REALTIME,&CloseTime);
							//send message close (== 0)
							 write(clients[n], "0\r\n", 7); 
							 printf("Close Time : %d.%d\n",CloseTime.tv_sec,CloseTime.tv_nsec);
							
						}
					}
				}
						//softServoWrite(RCSERVO,1250);
						//delay(4000);             
            }
        }    
		*/




/*
void ControlDoor(char *reqline)
{

		if(wiringPiSetupGpio () == -1) printf("servoMotor error\n", mesg);
		softServoSetup(RCSERVO,-1,-1,-1,-1,-1,-1,-1);	

		if 
		softServoWrite(RCSERVO,-250);
		write(clients[n], answer, sizeof(answer)); 
					

*/