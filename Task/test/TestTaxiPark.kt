package taxipark

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class TestTaxiPark {
    @Test
    fun testFakeDriversWhenThereAreNoTrips() {
        val tp = taxiPark(1..1, 1..1)
        assertEquals(drivers(1).toSet(), tp.findFakeDrivers(), "Wrong result for 'findFakeDrivers()'." + tp.display())
    }

    @Test
    fun testFakeDriversWhenOnlyOneDriverAndOneTrip() {
        val tp = taxiPark(1..1, 1..1, trip(1, 1))
        assertEquals(emptySet<Driver>(), tp.findFakeDrivers(), "Wrong result for 'findFakeDrivers()'." + tp.display())
    }

    @Test
    fun testFakeDriversWhenTaxiParkIsEmpty() {
        val tp = taxiPark(1 until 1, 1 until 1)
        assertEquals(emptySet<Driver>(), tp.findFakeDrivers(), "Wrong result for 'findFakeDrivers()'." + tp.display())
    }

    @Test
    fun testFakeDrivers() {
        val tp = taxiPark(1..3, 1..2, trip(1, 1), trip(1, 2))
        assertEquals(drivers(2, 3).toSet(), tp.findFakeDrivers(), "Wrong result for 'findFakeDrivers()'." + tp.display())
    }

    @Test
    fun testFaithfulPassengersWhenNumberOfTripsGivenIsZero() {
        val tp = taxiPark(1..4, 1..4, trip(1, 1), trip(2, 2), trip(3, 3))
        assertEquals(passengers(1, 2, 3, 4), tp.findFaithfulPassengers(0), "Wrong result for 'findFaithfulPassengers()'. MinTrips: 0." + tp.display())
    }

    @Test
    fun testFaithfulPassengersWhenThereAreNoTrips() {
        val tp = taxiPark(1..1, 1..1)
        assertEquals(passengers(), tp.findFaithfulPassengers(0), "Wrong result for 'findFaithfulPassengers()'. MinTrips: 0." + tp.display())
    }

    @Test
    fun testFaithfulPassengersWhenNumberOfTripsGivenIsNegative() {
        val tp = taxiPark(1..1, 1..1, trip(1, 1))
        assertEquals(passengers(1), tp.findFaithfulPassengers(-1), "Wrong result for 'findFaithfulPassengers()'. MinTrips: 0." + tp.display())
    }

    @Test
    fun testFaithfulPassengersWhenNumberOfTripsGivenIsHigherThanNumberOfTripsTaken() {
        val tp = taxiPark(1..1, 1..1, trip(1, 1))
        assertEquals(passengers(), tp.findFaithfulPassengers(2), "Wrong result for 'findFaithfulPassengers()'. MinTrips: 2." + tp.display())
    }

    @Test
    fun testFaithfulPassengers() {
        val tp = taxiPark(1..3, 1..5, trip(1, 1), trip(2, 1), trip(1, 4), trip(3, 4), trip(1, 5), trip(2, 5), trip(2, 2))
        assertEquals(passengers(1, 4, 5), tp.findFaithfulPassengers(2), "Wrong result for 'findFaithfulPassengers()'. MinTrips: 2." + tp.display())
    }

    @Test
    fun testFrequentPassengersWhenDriverHasNoFrequentPassengers() {
        val tp = taxiPark(1..1, 1..1, trip(1,1))
        assertEquals(passengers(), tp.findFrequentPassengers(driver(1)), "Wrong result for 'findFrequentPassengers()'. Driver: ${driver(1).name}." + tp.display())
    }

    @Test
    fun testFrequentPassengersWhenDriverHasOneFrequentPassenger() {
        val tp = taxiPark(1..1, 1..1, trip(1,1), trip(1,1))
        assertEquals(passengers(1), tp.findFrequentPassengers(driver(1)), "Wrong result for 'findFrequentPassengers()'. Driver: ${driver(1).name}." + tp.display())
    }

    @Test
    fun testFrequentPassengersWhenNeitherOfTwoDriversHasAFrequentPassenger() {
        val tp = taxiPark(1..2, 1..1, trip(1,1), trip(2,1))
        assertEquals(passengers(), tp.findFrequentPassengers(driver(1)), "Wrong result for 'findFrequentPassengers()'. Driver: ${driver(1).name}." + tp.display())
    }

    @Test
    fun testFrequentPassengersWhenOneOfTwoDriversHasAFrequentPassenger() {
        val tp = taxiPark(1..2, 1..1, trip(1,1), trip(2,1), trip(1,1))
        assertEquals(passengers(1), tp.findFrequentPassengers(driver(1)), "Wrong result for 'findFrequentPassengers()'. Driver: ${driver(1).name}." + tp.display())
    }

    @Test
    fun testFrequentPassengersWhenThereAreNoTrips() {
        val tp = taxiPark(1..2, 1..2)
        assertEquals(passengers(), tp.findFrequentPassengers(driver(1)), "Wrong result for 'findFrequentPassengers()'. Driver: ${driver(1).name}." + tp.display())
    }

    @Test
    fun testFrequentPassengersWhenDriverIsNotInTaxiPark() {
        val tp = taxiPark(1..2, 1..1, trip(1,1), trip(2,1), trip(1,1))
        assertEquals(passengers(), tp.findFrequentPassengers(driver(3)), "Wrong result for 'findFrequentPassengers()'. Driver: ${driver(1).name}." + tp.display())
    }

    @Test
    fun testFrequentPassengers() {
        val tp = taxiPark(1..2, 1..4, trip(1, 1), trip(1, 1), trip(1, listOf(1, 3)), trip(1, 3), trip(1, 2), trip(2, 2))
        assertEquals(passengers(1, 3), tp.findFrequentPassengers(driver(1)), "Wrong result for 'findFrequentPassengers()'. Driver: ${driver(1).name}." + tp.display())
    }

    @Test
    fun testSmartPassengersWhenPassengerHasOneTripWithoutDiscount() {
        val tp = taxiPark(1..1, 1..1, trip(1,1))
        assertEquals(passengers(), tp.findSmartPassengers(), "Wrong result for 'findSmartPassengers()'." + tp.display())
    }

    @Test
    fun testSmartPassengersWhenPassengerHasOneTripWithDiscount() {
        val tp = taxiPark(1..1, 1..1, trip(1,1, discount = 0.5))
        assertEquals(passengers(1), tp.findSmartPassengers(), "Wrong result for 'findSmartPassengers()'." + tp.display())
    }

    @Test
    fun testSmartPassengersWhenNoPassengersHaveDiscounts() {
        val tp = taxiPark(1..2, 1..2, trip(1, 1), trip(2, 2))
        assertEquals(passengers(), tp.findSmartPassengers(), "Wrong result for 'findSmartPassengers()'." + tp.display())
    }

    @Test
    fun testSmartPassengersWhenThereAreNoTrips() {
        val tp = taxiPark(1..2, 1..2)
        assertEquals(passengers(), tp.findSmartPassengers(), "Wrong result for 'findSmartPassengers()'." + tp.display())
    }

    @Test
    fun testSmartPassengersWhenAllPassengersHaveDiscounts() {
        val tp = taxiPark(1..2, 1..2, trip(1, 1, discount = 0.1), trip(2, 2, discount = 0.5))
        assertEquals(passengers(1, 2), tp.findSmartPassengers(), "Wrong result for 'findSmartPassengers()'." + tp.display())
    }

    @Test
    fun testSmartPassengersWhenTripHasMoreThanOnePassenger() {
        val tp = taxiPark(1..2, 1..3, trip(1, listOf(1,3), discount = 0.1), trip(2, 2))
        assertEquals(passengers(1, 3), tp.findSmartPassengers(), "Wrong result for 'findSmartPassengers()'." + tp.display())
    }

    @Test
    fun testSmartPassengers() {
        val tp = taxiPark(1..2, 1..2, trip(1, 1, discount = 0.1), trip(2, 2))
        assertEquals(passengers(1), tp.findSmartPassengers(), "Wrong result for 'findSmartPassengers()'." + tp.display())
    }


    @Test
    fun testTheMostFrequentTripDurationWhenThereAreNoTrips() {
        val tp = taxiPark(1..3, 1..5)
        assertEquals(null, tp.findTheMostFrequentTripDurationPeriod(), "Wrong result for 'findTheMostFrequentTripDurationPeriod()'.")
    }

    @Test
    fun testTheMostFrequentTripDurationWhenThereIsOneTripOf10Minutes() {
        val tp = taxiPark(1..3, 1..5, trip(1, 1, duration = 10))
        assertEquals(10..19, tp.findTheMostFrequentTripDurationPeriod(), "Wrong result for 'findTheMostFrequentTripDurationPeriod()'.")
    }

    @Test
    fun testTheMostFrequentTripDurationWhenThereIsOneTripOf20Minutes() {
        val tp = taxiPark(1..3, 1..5, trip(1, 1, duration = 20))
        assertEquals(20..29, tp.findTheMostFrequentTripDurationPeriod(), "Wrong result for 'findTheMostFrequentTripDurationPeriod()'.")
    }

    @Test
    fun testTheMostFrequentTripDurationWhenThereAreTwoTimesWithEqualNumberOfTrips() {
        val tp = taxiPark(1..3, 1..5, trip(1, 1, duration = 10), trip(3, 4, duration = 15),
                trip(1, 2, duration = 20), trip(2, 3, duration = 25))
        // The period 20..29 should be returned because there are more than one, so am returning the last one
        assertTrue("Wrong result for 'findTheMostFrequentTripDurationPeriod()'.") { 20..29 == tp.findTheMostFrequentTripDurationPeriod() || 10..19 == tp.findTheMostFrequentTripDurationPeriod() }
    }

    @Test
    fun testTheMostFrequentTripDuration() {
        val tp = taxiPark(1..3, 1..5, trip(1, 1, duration = 10), trip(3, 4, duration = 30),
                trip(1, 2, duration = 20), trip(2, 3, duration = 35))
        // The period 30..39 is the most frequent since there are two trips (duration 30 and 35)
        assertEquals(30..39, tp.findTheMostFrequentTripDurationPeriod(), "Wrong result for 'findTheMostFrequentTripDurationPeriod()'.")
    }

    @Test
    fun testParetoPrincipleSucceeds() {
        val tp = taxiPark(1..5, 1..4,
                trip(1, 1, 20, 20.0),
                trip(1, 2, 20, 20.0),
                trip(1, 3, 20, 20.0),
                trip(1, 4, 20, 20.0),
                trip(2, 1, 20, 20.0))
        // The income of driver #1: 160.0;
        // the total income of drivers #2..5: 40.0.
        // The first driver constitutes exactly 20% of five drivers
        // and his relative income is 160.0 / 200.0 = 80%.

        assertEquals(true, tp.checkParetoPrinciple(), "Wrong result for 'checkParetoPrinciple()'." + tp.display())
    }

    @Test
    fun testParetoPrincipleFails() {
        val tp = taxiPark(1..5, 1..4,
                trip(1, 1, 20, 20.0),
                trip(1, 2, 20, 20.0),
                trip(1, 3, 20, 20.0),
                trip(2, 4, 20, 20.0),
                trip(3, 1, 20, 20.0))
        // The income of driver #1: 120.0;
        // the total income of drivers #2..5: 80.0.
        // The first driver constitutes 20% of five drivers
        // but his relative income is 120.0 / 200.0 = 60%
        // which is less than 80%.

        assertEquals(false, tp.checkParetoPrinciple(), "Wrong result for 'checkParetoPrinciple()'." + tp.display())
    }

    @Test
    fun testParetoPrincipleNoTrips() {
        val tp = taxiPark(1..5, 1..4)
        assertEquals(false, tp.checkParetoPrinciple(), "Wrong result for 'checkParetoPrinciple()'." + tp.display())
    }

    @Test
    fun testParetoPrincipleOnlyOneDriver() {
        val tp = taxiPark(1..1, 1..3, trip(1,1,20,20.0), trip(1,2,20,20.0), trip(1,3,20,20.0))
        assertEquals(false, tp.checkParetoPrinciple(),"Wrong result for 'checkParetoPrinciple()'." + tp.display())
    }

    @Test
    fun testParetoPrincipleTwoDrivers() {
        val tp = taxiPark(1..2, 1..3,
                trip(1,1,20,20.0),
                trip(1,2,20,20.0),
                trip(1,3,20,20.0),
                trip(1,2,20,20.0),
                trip(2,2,20,20.0))
        assertEquals(false, tp.checkParetoPrinciple(),"Wrong result for 'checkParetoPrinciple()'." + tp.display())
    }

    @Test
    fun testParetoPrincipleThreeDrivers() {
        val tp = taxiPark(1..3, 1..3,
                trip(1,1,20,20.0),
                trip(1,2,20,20.0),
                trip(1,3,20,20.0),
                trip(1,2,20,20.0),
                trip(3,2,20,20.0))
        assertEquals(false, tp.checkParetoPrinciple(),"Wrong result for 'checkParetoPrinciple()'." + tp.display())
    }

    @Test
    fun testParetoPrincipleFourDrivers() {
        val tp = taxiPark(1..2, 1..3,
                trip(1,1,20,20.0),
                trip(1,2,20,20.0),
                trip(1,3,20,20.0),
                trip(1,2,20,20.0),
                trip(4,2,20,20.0))
        assertEquals(false, tp.checkParetoPrinciple(),"Wrong result for 'checkParetoPrinciple()'." + tp.display())
    }

    @Test
    fun testParetoPrincipleSixDriversTrue() {
        val tp = taxiPark(1..6, 1..4,
                trip(1, 1, 20, 20.0),
                trip(1, 2, 20, 20.0),
                trip(1, 3, 20, 20.0),
                trip(1, 4, 20, 20.0),
                trip(3, 1, 20, 20.0))
        assertEquals(true, tp.checkParetoPrinciple(), "Wrong result for 'checkParetoPrinciple()'." + tp.display())
    }

    @Test
    fun testParetoPrincipleSixDriversFalse() {
        val tp = taxiPark(1..6, 1..4,
                trip(1, 1, 20, 20.0),
                trip(1, 2, 20, 20.0),
                trip(1, 3, 20, 20.0),
                trip(2, 4, 20, 20.0),
                trip(6, 1, 20, 20.0))
        assertEquals(false, tp.checkParetoPrinciple(), "Wrong result for 'checkParetoPrinciple()'." + tp.display())
    }

    @Test
    fun testParetoPrincipleTenDriversTrue() {
        val tp = taxiPark(1..10, 1..4,
                trip(1, 1, 20, 20.0),
                trip(1, 2, 20, 20.0),
                trip(10, 3, 20, 20.0),
                trip(10, 4, 20, 20.0),
                trip(2, 1, 20, 20.0))
        assertEquals(true, tp.checkParetoPrinciple(), "Wrong result for 'checkParetoPrinciple()'." + tp.display())
    }

    @Test
    fun testParetoPrincipleTenDriversFalse() {
        val tp = taxiPark(1..6, 1..4,
                trip(1, 1, 20, 20.0),
                trip(2, 2, 20, 20.0),
                trip(3, 3, 20, 20.0),
                trip(6, 4, 20, 20.0),
                trip(10, 1, 20, 20.0))
        assertEquals(false, tp.checkParetoPrinciple(), "Wrong result for 'checkParetoPrinciple()'." + tp.display())
    }

    @Test
    fun cumulativeIncomeFunctionTestA() {
        val incomes = listOf(1.0, 2.0, 3.0)
        assertEquals(listOf(1.0, 3.0, 6.0), calculateCumulativeIncome(incomes))
    }

    @Test
    fun findAnyParetoPrincipleTestA() {
        val cumulativeIncomes = listOf(120.0, 160.0, 200.0, 200.0, 200.0)
        val totalIncome = 200.0
        assertEquals(false, findAnyParetoPrinciple(cumulativeIncomes, totalIncome))
    }

}