#include <stdio.h>
#include <math.h>

#define BODY 10
#define FUEL 8
#define CONSUME 0.8
#define THRUST 500
#define GRAVITY 9.81
#define K 0.6
#define A 0.1

void calculations(double dt, double *xmax, double *duration);
void print_seq(double height, double t, double v);

int main(int argc, char* argv[]){ //meant to do it based on the 40 lines and 72 width restriction ??
    double deltat = 0, xmax = 0, duration = 0;
    //double duration, max_alt;
    printf("Enter deltat: ");
    scanf("%lf", &deltat);
    printf("Seconds\n 0.0|>>=>\n");
    calculations(deltat, &duration, &xmax);
    printf("Maximum altitude: %.1lf metres, duration: %.1lf seconds",duration , xmax);

}

void calculations(double dt, double *xmax, double *duration){// trajectory calculator, orient up as positive
    double mass, push;
    double grav;
    double air = 0, v = 0, t = 0;
    double a, force = 0.0, x = 0.0;
    // form an equation for velocity
    while(x >= 0){
        t += 1;
        if(t < 10){// in stage 1
            mass = BODY + (FUEL - 0.8*t);
            air = -K*A*pow(v,2);
            push = THRUST;
        }
        else if (t > 10 && v >= 0){// past stage one
            push = 0;
            mass = BODY;
            air = -K*A*pow(v,2);
        }
        else {//stage 3
            air = K*A*pow(v,2);
            mass = BODY;
            push = 0;
        }
        grav = -mass*GRAVITY;
        force = push + air + grav;
        a = force/mass;
        v += a*dt;
        x += v*dt;
        //printf("a: %lf, v: %lf, x: %lf, t: %lf\n",a,v,x,t);
        print_seq(x, t, v);
        if (x > *xmax){
            *xmax = x;
        }
        if(t > *duration){
            *duration = t;
        }
    }
}

void print_seq(double height, double t, double v){
    printf(" %1.1lf|", t);
    int spaces = 0;
    //printf("height: %lf,spaces: %d\n", height, spaces);
    while (spaces < height){
        if(spaces%8 == 0){
            printf(" ");
        }
        spaces++;
    }
    if (v >= 0){
        if (t <= 10){
            printf(">>=>\n");
        }
        else{
            printf("===>\n");
        }
    }
    else{
        printf("<===\n");
    }
}