将Recv04_01.java 文件复制几份 Recv04_02.java   Recv04_03.java 

然后执行Recv04_01 与 Recv04_02

接下来执行Sender04发送消息，可以看到Recv04_01 与Recv04_02都接收到了消息。

然后执行Recv04_03，没有获取到任何消息。

接下来再执行Sender04发送消息，可以看到Recv04_01 、Recv04_02与Recv04_03都接收到了消息。

说明Exchange在收到生产者的消息后，会将消息发送给当前已经与它绑定了的所有Queue 。  然后被移除。