The project is a simple command line application for invoice generation.
Reading input information from stdin, write generated invoices into stdout.

Used Java 17, but all used features not higher than Java 8 (streams)

Dependencies :
    google guava
    lombok
They are not principal, but just for writing simplicity, could be easily removed if needed.

Building :
    mvn clean install

Input format :
    PackageType NumberOfSMS NumberOfMinutes
PackageType : S, M, L
Example :
    s 0 5
    m 99 99
    L   8  99


Implementation remarks :
1. Implemented 2 versions of triffs and 2 different calculators
    1.1 "Simple" tariff system (finnbyte.model.simpleTariff package).
        For each package keep 3 values - limit for SMS / Minutes and fixed price.
    1.2 "Range" tariff system (finnbyte.model.tariff package)
        For each package store group of ranges with different unit prices, so mor then one range could be applied.

2. Both Tariff databases are initialised from hardcoded values in code.

3. Test are located in src/test directory (both versions of calculator are tested by the same test AbstractCalculatorTest)

3. TO-DO
    - add currency support (applied just currency label)
    - add currency conversion / validation
    - database initialisation from external storage
    - more accurate implementation of tariff ranges (continious) !!!!
