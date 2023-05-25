package main

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import scala.concurrent.ExecutionContextExecutor
import scala.language.postfixOps

case class ReadJsonFile(file: String)
case class JsonProcessedMsg()
case class StartProcessFileMsg()


class JsonActor extends Actor {
  def receive: Receive = {
    case ReadJsonFile(file) => {
      val data = ujson.read(file)
      println("request id is: " +  data("id"))
      println("user agent is: " +  data.obj.get("device").toString.split(",")(2).substring(4))
      val jsonString = os.read(os.pwd/"src"/"main"/"scala"/"resources"/"response.json")
      println("the response is:")
      println(jsonString)
      sender ! JsonProcessedMsg()
    }
  }
}


class BidRequestActor(json: String) extends Actor {

  private var fileSender: Option[ActorRef] = None

  def receive: Receive = {
    case StartProcessFileMsg() => {
      fileSender = Some(sender)
      context.actorOf(Props[JsonActor]) ! ReadJsonFile(json)
    }
  }
}

object Main extends App {

  import akka.dispatch.ExecutionContexts._
  import akka.pattern.ask
  import akka.util.Timeout
  import scala.concurrent.duration._

  implicit val ec: ExecutionContextExecutor = global
  implicit val timeout: Timeout = Timeout(25 seconds)
  val system = ActorSystem("System")
  val jsonString = os.read(os.pwd/"src"/"main"/"scala"/"resources"/"request.json")
  val actor = system.actorOf(Props(new BidRequestActor(jsonString)))
  val future = actor ? StartProcessFileMsg()
  system.terminate()
//  future.map { result =>
//    println("")
//    system.terminate()
//  }

}
