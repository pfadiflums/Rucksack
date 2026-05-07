plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "rucksack"

include("domain", "infrastructure", "application", "api")