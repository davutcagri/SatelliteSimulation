# SatelliteSimulation

A small satellite simulation I made to practice orbital mechanics and building a system out of separate services.

The Earth orbits the Sun and a satellite orbits the Earth, integrated with RK4 from scratch. The satellite has a solar array, a battery and a simple autonomy system that switches between nominal, power saving and safe mode when the battery runs low. A web ground station shows the orbit, the battery level and the satellite state in real time, and lets you change the simulation speed, enable the payload and inject a solar array fault.

## How it works

There are two Java services, the satellite and the ground station backend, talking over a WebSocket. The satellite sends telemetry and the ground station sends commands back. The web UI is written in TypeScript with React and only talks to the ground station backend.

## Run

You need Java 17 and Node. Build the Java services:

    mvn -DskipTests package

Start the satellite (port 8081), then the ground station backend (port 8080):

    java -jar satellite/target/satellite-0.1.0-SNAPSHOT.jar
    java -jar ground-station-backend/target/ground-station-backend-0.1.0-SNAPSHOT.jar

Then start the UI:

    cd ground-station-ui
    npm install
    npm run dev

Open http://localhost:5173. The default speed is real time, so pick 600x at the top to see a full orbit in a few seconds.
