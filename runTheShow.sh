#!/bin/bash
#	 * 1 [0] [portnr] or FIND, FIND will find a new port from 1337 and up.
#	 * 2 [1] [username] or GENERIC, Generic will give a generic username.
#	 * 3 [2] [ns:True/False], True to use nameserver.
#	 * 4 [3] [server:ip] not CREATE, ip or hostname for nameserver connecting to.
#	 * 5 [4] [server:port], port for server connecting to.
#	 * 6 [5] [groupname] (Optional) connect to group in given ns
#	 *
#	 * 1 [0] [portnr] or FIND, FIND will find a new port from 1337 and up.
#	 * 2 [1] [username] or GENERIC, Generic will give a generic username.
#	 * 3 [2] [ns:True/False], True to use nameserver.
#	 * 4 [3] [CREATE], to create a chatgroup
#	 * 5 [4] [groupname] name of chatgroup to create
#	 * 6 [5] [MO] where MO is UO, CAUSAL, FIFO
#	 * 7 [6] [CO] where CO is RM, UM, TREE

javaCMD="java -jar client.jar"
PORT=0
function pause(){
   read -p'Ready? - [ENTER] to continue...'
   echo ''
}

function killalljava() {
    for KILLPID in `ps ax | grep "$javaCMD" | awk ' { print $1;}'`; do 
        kill -9 $KILLPID;
    done
}


read LOWERPORT UPPERPORT < /proc/sys/net/ipv4/ip_local_port_range
while :
do
    PORT="`shuf -i $LOWERPORT-$UPPERPORT -n 1`"
    ss -lpn | grep -q ":$PORT " || break
done


function cola() {
    killalljava
    sleep 1
    echo "COLA"
    echo "This will create 10 clients in one group using Unordered and Reliable."
    echo "Show that many messages is sent in debugger."
    pause
    eval $javaCMD $PORT TheLeader True CREATE Colagruppen UO RM &
    sleep 2
    for i in {2..10}
    do
        eval $javaCMD FIND GENERIC True localhost $PORT Colagruppen &
        sleep 1
    done
    sleep 1
    pause
}

function pepsi() {
    killalljava
    sleep 1
    echo "PEPSI"
    echo "This will create 3 clients in one group using Casual and Unreliable."
    pause
    eval $javaCMD $PORT A True CREATE PepsiGruppen CASUAL UM &
    sleep 2
    eval $javaCMD FIND B True localhost $PORT PepsiGruppen &
    sleep 1
    eval $javaCMD FIND C True localhost $PORT PepsiGruppen &
    pause
    killalljava
}

#FIFO DO NOT RUN
function zingo() {
    killalljava
    sleep 1
    echo "ZINGO"
    echo "This will create 3 clients in one group using FIFO and Unreliable."
    eval $javaCMD $PORT A True CREATE ZingoGruppen FIFO UM &
    sleep 2
    eval $javaCMD FIND B True localhost $PORT ZingoGruppen &
    sleep 1
    eval $javaCMD FIND C True localhost $PORT ZingoGruppen &
    pause
    killalljava
}

function coconut() {
    killalljava
    sleep 1
    echo "COCONUT"
    echo "This will create 5 clients in one group using Unordered and Tree."
    pause
    eval $javaCMD $PORT A True CREATE GENDERISTIC UO TREE &
    sleep 2
    eval $javaCMD FIND B True localhost $PORT GENDERISTIC &
    sleep 1
    eval $javaCMD FIND C True localhost $PORT GENDERISTIC &
    sleep 1
    eval $javaCMD FIND D True localhost $PORT GENDERISTIC &
    sleep 1
    eval $javaCMD FIND E True localhost $PORT GENDERISTIC &
    pause
    killalljava
}

cola
pepsi
#zingo
coconut

