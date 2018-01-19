# First Remote Playground

## Download and setup 

- `git clone http://im-lamport:1080/dc/firstremotes.git`
- Make sure to set your local ip address or hostname in `/src/main/resource/application.conf`. Make sure to NOT use `localhost`or `127.0.0.1`

## Prepare playground

- `sbt update`

- `sbt console`

- Within sbt console

  - `import akka.actor._`  
    `import akka.remote._`
    `import akka.remote.routing._`
    `import akka.routing._`
    `import akka.cluster.routing._`
    `import com.typesafe.config._`
    `import actor._`
  - `val config = ConfigFactory.load()`
  - `val system = ActorSystem("my-remote-sys", config)`

## Remotely deploy new actor

- `val remoteSysPath = AddressFromURIString("akka.tcp://my-remote-sys@<HOSTNAME>:<PORT>")`
- `val remoteActor = system.actorOf(Props(classOf[SimpleResponder], "John").withDeploy(Deploy(scope=RemoteScope(remoteSysPath))))`
  
  This will create an actor at a remote node on machine with *<HOSTNAME>:<PORT>* and send a string message. If you don't receive a reply it is most likely your hostname/ip (set above) is not reachable from remote host.
  
## Create remote routing pool

- **Exercise**: 
  Create a remote routing pool and send messages to it. Check whether printouts meet the expected behavior.
- **Solution**:
  - `val nodes = List(AddressFromURIString("akka.tcp://my-remote-sys@localhost:2551"), AddressFromURIString("akka.tcp://my-remote-sys@localhost:2552") )`
  - `val rr1 = system.actorOf( RemoteRouterConfig(RoundRobinPool(5), nodes ).props(Props[SimplePrinter]), "router1")` 

## Create cluster routing pool

- **Exercise**: Create a cluster routing pool
  - Switch to `akka.cluster.ClusterActorRefProvider` in `application.conf`
  - Set `totalInstances` to a high number and `maxInstancesPerNode` very low
- **Solution**: 
  - `val cr2 = system.actorOf(ClusterRouterPool(RandomPool(0),ClusterRouterPoolSettings(totalInstances = 100, maxInstancesPerNode = 3, allowLocalRoutees = false)).props(Props[SimplePrinter]),name = "my-random-router")`
  - send messages, e.g. `(1 to 30).foreach( i => cr2 ! s"$i. message")`
  - Start up another node, the router should create new actors there and start routing to them too.

## Create cluster sharding for stateful actors

- **Exercise**: Create stateful actors (cf. `SimpleStateKeeper) and spread instances over various nodes using *Sharding*. 
  - implement functions `ExtractShardId` and `ExtractEntityId` (e.g. in `object SimpleStateKeeper`)
  - As you create new and remove existing nodes the actor instances should be moved in the shards across the nodes
  - See documentation for a simple example: https://doc.akka.io/docs/akka/current/cluster-sharding.html?language=scala 
  
