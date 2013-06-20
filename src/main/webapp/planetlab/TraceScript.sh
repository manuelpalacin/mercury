cat domains.txt | while read domain; 
	do 
		rm -rf $domain.txt;
		traceroute -n $domain >> $domain.txt; 	
		destination=`head -1 $domain.txt`;
		#echo $destination;
		destinationip=`echo "$destination" | grep -E -m 1 -o '(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)' | head -n 1`;
	
		lines=`wc -l < $domain.txt`;
		lines=`expr $lines - 1`;
		traces=`tail -$lines $domain.txt`;
		hops='';
		echo "$traces" | while IFS= read line;
		do
			#echo $line;
			id=`echo "$line" | grep -E -m 1 -o '(^[ ][1-9]|^[1-3][0-9])' | head -n 1`;
			#remove white spaces			
			id=$(echo "$id" | tr -d ' ');
			ip=`echo "$line" | grep -E -m 1 -o '(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)' | head -n 1`;
			#if ip is not pattern matched --> * * *			
			if [ -z "$ip" ]; then
				ip='destination unreachable';
			fi	
			#echo '{"id":"'$id'","ip":"'$ip'"}';
			hops=$hops',{"id":"'$id'","ip":"'$ip'"}';
			echo $hops > hops$domain.txt;
		done;
		hops=`cat hops$domain.txt`;
		#to remove the first comma		
		hops=$(echo "$hops" | cut -c 2-);
		traceroute='{"destination":"'$domain'","ip":"'$destinationip'","hops":['$hops']}';
		rm -rf $domain.txt hops$domain.txt;
		curl -X POST -H "Content-Type: application/json" -d "$traceroute" http://193.145.50.220/mercury/api/traceroute/uploadTrace;
	done;



