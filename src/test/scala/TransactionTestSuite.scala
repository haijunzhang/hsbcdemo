import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class TransactionTestSuite extends Simulation {
  val httpProtocol = http
    .baseUrl("http://localhost:8088") // Base URL for the API
    .acceptHeader("application/json") // Accept header for JSON responses

  // 先执行创建交易的测试
  val createTest = new TransactionSimulation().scn
  // 然后执行查询交易的测试
  val getTest = new GetTransactionSimulation().getTransactionsScn

  setUp(
    // 按顺序执行测试
    createTest.inject(
      rampUsers(100).during(10.seconds),
      constantUsersPerSec(10).during(20.seconds)
    ),
    getTest.inject(
      nothingFor(30.seconds), // 等待创建测试完成
      rampUsers(50).during(5.seconds),
      constantUsersPerSec(5).during(10.seconds)
    )
  ).protocols(httpProtocol)
} 