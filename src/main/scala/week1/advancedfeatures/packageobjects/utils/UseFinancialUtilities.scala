package week1.advancedfeatures.packageobjects.utils

/**
 * use of the enhanced package object to manage financial accounts,
 * leveraging type aliases and case classes to perform operations like interest application.
 */
object UseFinancialUtilities extends App {
  log("Initializing financial operations...")

  val accounts: AccountMap = Map(
    "acc001" -> 1000.0,
    "acc002" -> 1500.0,
    "acc003" -> 1200.0
  )

  val updatedAccounts = applyInterest(accounts, 0.05)
  log("Updated account balances with interest:")
  updatedAccounts.foreach { case (id, balance) => log(s"Account $id: $balance") }
}