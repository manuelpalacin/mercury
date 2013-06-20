rm good_connectivity no_auth_connectivity other_connectivity good_tr not_found other_issues activenodes.txt;
command="traceroute";
timeout="5";
cat nodes.txt | while read node; do
    echo "checking node: "$node;
    #check ssh connectivity
       status=$(ssh -o StrictHostKeyChecking=no -o ConnectTimeout="$timeout" -o BatchMode=yes -i planetlabkeys/id_rsa upfple_mercury@$node echo ok 2>&1 < /dev/null);
    echo "status: "$status;
    if [[ $status == ok ]] ; then
        echo $node >> good_connectivity
    elif [[ $status == "Permission denied"* ]] ; then
        echo $node $status >> no_auth_connectivity
    else
        echo $node $status >> other_connectivity
    fi

    #check traceroute command
    if [[ $status == ok ]] ; then
        trinstalled=$(ssh -o StrictHostKeyChecking=no -o ConnectTimeout="$timeout" -o  BatchMode=yes -i planetlabkeys/id_rsa upfple_mercury@$node $command < /dev/null;echo $?);
        echo "traceroute: "$trinstalled;
        if [[ $trinstalled == 0 ]] ; then
            echo $node >> good_tr
        elif [[ $trinstalled == 127 ]] ; then
            echo $node $trinstalled >> not_found
        else
            echo $node $trinstalled >> other_issues
        fi

        #check if node has ssh connectivity and traceroute command
        if [ $status == ok ] && [ $trinstalled == 0 ] ; then
            echo 'active node: '$node;
            echo $node >> activenodes.txt;
        else 
            echo "no connection or no traceroute";        
        fi

    else
        echo "no connection";
    fi
done;