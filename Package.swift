// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "AutorunifyCapacitorNrfmesh",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "AutorunifyCapacitorNrfmesh",
            targets: ["NrfMeshPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "8.0.0")
    ],
    targets: [
        .target(
            name: "NrfMeshPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/NrfMeshPlugin"),
        .testTarget(
            name: "NrfMeshPluginTests",
            dependencies: ["NrfMeshPlugin"],
            path: "ios/Tests/NrfMeshPluginTests")
    ]
)