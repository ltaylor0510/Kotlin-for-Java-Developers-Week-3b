package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> = this.allDrivers - driversWhoMadeTrips()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        if (minTrips == 0) allPassengers else passengersWhoMadeTrips().groupByCount().filterValues { it >= minTrips }.keys

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        passengersOf(driver).groupByCount().filterValues { it > 1 }.keys

/*

 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    return passengersWhoMadeTrips()
            .map { it to discountsFor(it) }
            .toMap()
            .mapValues { it.value.count { discount -> discount != null } > it.value.size / 2 }
            .filterValues { it }.keys
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? = if (this.trips.isEmpty()) null else mostFrequentTripDurationPeriod()

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean = if (trips.isEmpty() || allDrivers.size < 5) false else incomeSplitAdheresToParetoPrinciple()

private fun TaxiPark.incomeSplitAdheresToParetoPrinciple(): Boolean {
    val eachDriversIncome = allDrivers.fold(emptyList<Double>()) { driversIncome, driver -> driversIncome + incomeOf(driver) }.sortedDescending()
    val totalIncome = eachDriversIncome.reduce { acc, d -> d + acc }
    val cumulativeIncomeList = calculateCumulativeIncome(eachDriversIncome)

    return findAnyParetoPrinciple(cumulativeIncomeList, totalIncome)
}

fun findAnyParetoPrinciple(cumulativeIncomeList: List<Double>, totalIncome: Double) =
        cumulativeIncomeList.withIndex().any { (it.index + 1).toDouble() / cumulativeIncomeList.size <= .2 && it.value / totalIncome >= .8 }

fun calculateCumulativeIncome(eachDriversIncome: List<Double>) =
        eachDriversIncome.foldIndexed(emptyList<Double>()) { index, cumulativeIncome, driverIncome -> if (index == 0) cumulativeIncome + driverIncome else cumulativeIncome + (driverIncome + cumulativeIncome[index - 1]) }

private fun TaxiPark.incomeOf(driver: Driver): Double = trips.fold(0.0) { acc, trip -> if (trip.driver == driver) trip.cost + acc else acc }

private fun TaxiPark.driversWhoMadeTrips() = trips.map { it.driver }

private fun TaxiPark.passengersWhoMadeTrips() = trips.flatMap { it.passengers }

private fun TaxiPark.passengersOf(driver: Driver) = trips.filter { it.driver == driver }.flatMap { it.passengers }


private fun TaxiPark.discountsFor(passenger: Passenger) =
        trips.filter { passenger in it.passengers }.map { it.discount }


private fun TaxiPark.mostFrequentTripDurationPeriod(): IntRange? {
    return mapDurationsToTripCount().maxBy { it.value }?.key
}

private fun TaxiPark.mapDurationsToTripCount(): Map<IntRange, Int> {
    return tripDurationPeriods().map { it to trips.filter { trip -> trip.duration in it }.count() }.toMap()
}

private fun TaxiPark.tripDurationPeriods(): List<IntRange> = (0..maxTripDuration() step 10).map { it -> IntRange(it, it + 9) }


private fun TaxiPark.maxTripDuration() = trips.map { it.duration }.max() ?: 0

fun <T> Iterable<T>.groupByCount(): Map<T, Int> =
        this.groupingBy { it }.eachCount()
