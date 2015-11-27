#include <wiringPi.h>
#include "softServo.h"

#include <stdio.h>
#include <stdlib.h>
#define RCSERVO 23

int main(void)
{
int i=0;
if(wiringPiSetupGpio () == -1)
return -1;

softServoSetup(RCSERVO,-1,-1,-1,-1,-1,-1,-1);

while(1)
	{
softServoWrite(RCSERVO,-250);
delay(4000);

softServoWrite(RCSERVO,1250);
delay(4000);
	}

}
