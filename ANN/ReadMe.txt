Chad Collins
CSCI402: Assignment 3/Final BPANN
Dec 9 2016

This is my submission for the final project. The same names are used as the assignment 3 package, as this is just an updated version.

This program was written in Windows using Eclipse. However, it should also run fine in a linux environment.
The MAIN_ANN.java is the main file to be run. Make sure that the agaricus-lepiota.data file is in the correct directory to run from the file. Otherwise, there is an unused 
commented out portion towards the top of the file to allow an input argument instead.
Also included are the Neuron.java, ProcessInput.java, ProcessOutput.java, and NetworkConnection.java helper classes.
There is also a sample testing-data.txt, training-data.txt, and results.txt file from my last execution. All result data is written to the results.txt file.

For this BPANN assignmnent, my major problems I ran into was getting my training data to converge and come up with a progressive result. My initial results
were all nonsense, and it took me a while to find out why that was the case. I originally thought it was a problem with my algorithm, but I eventually
ended up changing the way I input my data which solved the issue. I originally had it input as just a conversion from characters to their respective ASCII numbers. 
These numbers range from -1 to 50, which was too high for my algorithm. I eventually just divided the numbers by 50 and took the absolute value, after doing this, 
I got all numbers between 0 and 1 which was much more easy to deal with. After coming over this hurdle, everything else fell into place a bit easier.

The speed and accuracy I noticed peaked at around 4 hidden nodes. Currently, the training will stop when either the training success rate is equal to 99.9%, or 2000 epochs
are reached. The problem is that it can be a bit slow, taking thousands of epochs to get a very good result.
However, even after a hundred epochs my training result is around a 99% accuracy. This only takes a few thousand milliseconds, which feels very fast compared to the 50 thousand
or so it takes to reach 2000 epochs. However, if I stop at a 99% training accuracy, the test accuracy usually suffers, sitting around 85% accuracy.

In order to gain higher accuracy without sacrificing running time, I think adding more hidden layers would help. I know that my program spends a lot of time opening
and closing the result file to write to as well, which I could have made more efficient to improve running time. Outside of having a faster algorithm,
I'm unsure of any other ways to satisfy a high accuracy without sacrificing running time, as the nature of back propagation in an artificial neural 
network is that it takes time to learn to solve a problem.