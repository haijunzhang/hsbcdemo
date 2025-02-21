import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.util.Random
import java.util.UUID

class TransactionSimulation extends Simulation {
  val httpProtocol = http
    .baseUrl("http://localhost:8088") // Base URL for the API
    .acceptHeader("application/json") // Accept header for JSON responses
    
  val scn = scenario("Transaction Test")
    .exec(http("create_transaction")
      .post("/api/transactions") // POST request to create a new transaction
      .body(StringBody(session => {
        val randomPayerId = Random.nextLong(1000000000L)
        val randomPayeeId = Random.nextLong(1000000000L)
        val randomAmount = Random.nextInt(1000)
        val transactionNo = UUID.randomUUID().toString
        s"""{"transactionNo":"$transactionNo","amount":$randomAmount,"type":"PAYMENT","status":"SUCCESS","payerId":$randomPayerId,"payeeId":$randomPayeeId}"""
      })) // Body of the request with random transactionNo, random amount, and random 10-digit payerId and payeeId
      .asJson) 
    .pause(1) // Pause for 1 second

  setUp(
    scn.inject(
      rampUsers(100).during(10.seconds), //  100 users over 10 seconds
      constantUsersPerSec(10).during(20.seconds) // Maintain 10 users per second for 20 seconds
    ).protocols(httpProtocol)
  )

} 