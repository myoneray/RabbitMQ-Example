将Sender05.java 中的messageType分别改为type02  type03 然后发送消息 ， 
可以看到消费者端能接收到type02的消息，但是不能接收到type03的消息。
1>>>String messageType = "type02";
1>>>received message[Maizi] from exchange02

2>>>String messageType = "type03";
2>>>send success!  