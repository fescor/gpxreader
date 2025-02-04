This is a simple Map Reduce framework implemented in Java for the course project of Distributed 
System at the Computer science department in Athens University of Economics and Buisiness.

Implementation :
Worker , Worker2 , Worker3 are the reducers and MasterMain is the Maper.MasterMain collects 
Data from the Application and sends to the workers chunks of that data to do some calculations
who then return the results back to MasterMain and master main is responsible for
sorting , grouping and forwarding back the data to the application. 
