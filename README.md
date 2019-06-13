# openaq-apis

This Kotlin library is the an alpha version to access the Open AQ services https://openaq.org/
following the documentation : https://docs.openaq.org/#api-Measurements 

Actually only the https://docs.openaq.org/#api-Measurements API is available

## Usage 
```kotlin
        val oaq = OAQ(null)
        val from = Instant.now().minusSeconds(60 * 60 *24) // 4 day of data
        val to = Instant.now()

        val measurements = oaq.getMeasurementsByCoord("44.841225", "-0.580036",  start = from, end= to)

```
