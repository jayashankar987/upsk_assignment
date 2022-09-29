plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    id("io.gitlab.arturbosch.detekt") version "1.22.0-RC1"
}

dependencies {
    implementation("com.android.tools.build:gradle:7.1.3")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.21.0")
}

tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile::class.java).configureEach {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.name
}

detekt {
    buildUponDefaultConfig = true
    config.from(file("../../detekt.yml"))
}

gradlePlugin {
    plugins {
        create("myapp-collect-sarif") {
            id = "myapp-collect-sarif"
            implementationClass = "CollectSarifPlugin"
        }
        create("myapp-lint") {
            id = "myapp-lint"
            implementationClass = "LintPlugin"
        }
        create("myapp-detekt") {
            id = "myapp-detekt"
            implementationClass = "DetektPlugin"
        }
    }
}
