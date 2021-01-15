package com.udacity.asteroidradar.database

import com.udacity.asteroidradar.Asteroid

fun List<AstroidEntity>.convertToAstroifDataClass():List<Asteroid>{
    return this.map {
        Asteroid(
            id=it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )

    }

}