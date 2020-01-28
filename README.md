# chatserver
netty practice

1. ChatServer.java 기동
2. 순차적으로 ChatClient.java 기동
3. 각 client 터미널에서 다음과 같은 포맷으로 메시지 전송
  - '%userId %content' (사용자id (whitespace) 내용)
  - ex. 2 hello this is a text
4. 사용자 '0'번에게 메시지 전송시 접속되어 있는 사용자 모두에게 broadcast 메시지 전송
