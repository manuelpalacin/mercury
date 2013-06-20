# Download updated scripts.
echo Downloading the last script and configuration version...
wget -N http://193.145.50.220/mercury/planetlab/TraceScript.sh
wget -N http://193.145.50.220/mercury/planetlab/domains.txt
echo Done!

#Change script permission.
chmod +x ./TraceScript.sh

#Execute script.
echo Executing Mercury...
./TraceScript.sh
echo Done!