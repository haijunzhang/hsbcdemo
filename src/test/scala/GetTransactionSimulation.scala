import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.util.Random
import java.util.UUID

class GetTransactionSimulation extends Simulation {
  val httpProtocol = http
    .baseUrl("http://localhost:8088") // Base URL for the API
    .acceptHeader("application/json") // Accept header for JSON responses


  val getTransactionsScn = scenario("Get Transactions Test")
    .exec(http("get_transactions")
      .get("/api/transactions")
      .queryParam("page", session => Random.nextInt(10).toString) // Random page number between 0 and 9
      .queryParam("size", "10")
      .check(status.is(200)))
    .pause(1) // Pause for 1 second

  setUp(
    getTransactionsScn.inject(
      rampUsers(100).during(10.seconds), // 100 users over 10 seconds
      constantUsersPerSec(10).during(20.seconds) // Maintain 10 users per second for 20 seconds
    ).protocols(httpProtocol),
    getTransactionsScn.inject(
      rampUsers(100).during(10.seconds), // 100 users over 10 seconds
      constantUsersPerSec(10).during(20.seconds) // Maintain 10 users per second for 20 seconds
    ).protocols(httpProtocol)
  )
}
