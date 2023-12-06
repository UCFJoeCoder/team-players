package com.ucfjoe.teamplayers.domain.model

data class Player(val id: Long = 0, val teamId: Long, val jerseyNumber: String) :
    Comparable<Player> {

    /**
     * Compares this object with the specified object for order. Returns zero if this object is equal to the specified other object, a negative number if it's less than other, or a positive number if it's greater than other.
     *
     * Compares two Players by their JerseyNumber. Rules are, numbers are compared as numbers and come before strings, which are compared as strings.
     *
     * Example list ["AA", "01", "1", "10", "2", "BB", "1A", "20"]
     * When sorted becomes ["01", "1", "2", "10", "20", "1A", "AA", "BB"]
     *
     * @param other another instance of a Player
     * @return -1 of this is less than other, 1 of this is greater then other, 0 if this and other are equal
     */
    override fun compareTo(other: Player): Int {
        val selfJerseyIsNumber = jerseyNumber.toIntOrNull() != null
        val otherJerseyIsNumber = other.jerseyNumber.toIntOrNull() != null

        return if (selfJerseyIsNumber != otherJerseyIsNumber)
            if (selfJerseyIsNumber) -1 else 1
        else if (selfJerseyIsNumber)
            jerseyNumber.toInt() - other.jerseyNumber.toInt()
        else
            jerseyNumber.compareTo(other.jerseyNumber)
    }
}
