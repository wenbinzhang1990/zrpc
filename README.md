# zrpc
a simple rpc framework,small but complete
代码结构:
client:测试使用,存放api
service:测试使用,存放api实现
test:测试使用,单元测试所在
core:核心代码
  client:客户端使用
    balance:负载均衡相关实现
    proxy:动态代理
    stub:服务存根,通过stub调用远程服务
      disvoery:服务发现
      serviceContext:服务上下文
  annotation:注解
  server:服务端使用
    registry:服务注册使用
  transport:netty相关,包括序列化,协议等等
  zookeeper:注册中心相关,使用zookeeper
  
