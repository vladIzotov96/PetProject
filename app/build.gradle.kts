plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("kotlin-parcelize")
	id("androidx.navigation.safeargs")
	id("com.google.devtools.ksp")
}

android {
	namespace = "com.example.heroespractice"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.example.heroespractice"
		minSdk = 26
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	viewBinding {
		enable = true
	}
	dataBinding {
		enable = true
	}
}

dependencies {

	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")

	implementation("androidx.core:core-ktx:1.13.0")
	implementation("androidx.appcompat:appcompat:1.7.0")
	implementation("com.google.android.material:material:1.12.0")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")

	testImplementation("junit:junit:4.13.2")

	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

	implementation("com.squareup.okhttp3:okhttp:4.11.0")
	implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")

	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation("com.squareup.retrofit2:adapter-rxjava2:2.3.0")
	implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
	implementation("com.github.bumptech.glide:glide:4.15.1")

	implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

	implementation("androidx.core:core-splashscreen:1.0.0-alpha02")

	implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
	implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

	/**WorkManager*/
	implementation("androidx.work:work-runtime-ktx:2.7.0")

	/**Room*/
	implementation("androidx.room:room-runtime:2.5.2")
	annotationProcessor("androidx.room:room-compiler:2.5.2")
	implementation("androidx.room:room-rxjava2:2.6.1")
	ksp("androidx.room:room-compiler:2.5.2")

	/**A library that allows to easily add a ‘shimmer’ effect to UI elements.*/
	implementation("com.facebook.shimmer:shimmer:0.5.0")

	/**Koin*/
	implementation("io.insert-koin:koin-android:3.5.0")
}
