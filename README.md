# Daily Task Tracker 🚀

A sleek, lightweight desktop application designed to help users manage daily productivity. Built with **JavaFX** for a native UI experience and **Spring Framework** for robust dependency management and local persistence.

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Spring](https://img.shields.io/badge/Spring-6.0-green)

## ✨ Features

- **Daily Task Management:** Easily add, track, and complete tasks for the current day.
- **Historical Reports:** View your productivity trends over time with the Reports view.
- **Local Persistence:** Data is automatically saved to your user home directory in a JSON format.
- **Native Experience:** Includes a custom installer (.msi) with desktop shortcuts and a start menu entry.
- **Smooth Navigation:** Interactive Sidebar/NavBar for seamless view switching.

## 🛠️ Technical Stack

- **UI Framework:** JavaFX 21
- **Backend Core:** Spring Framework (Context & IoC)
- **Data Handling:** Jackson (JSON Serialization)
- **Project Management:** Maven
- **Installer Tool:** WiX Toolset + jpackage

## 📸 Screenshots

| Today's Tasks | Productivity Reports |
|---|---|
| ![Today View](https://via.placeholder.com/400x250?text=Today+Task+View) | ![Report View](https://via.placeholder.com/400x250?text=Reports+View) |

## 🚀 Getting Started

### Prerequisites
* **JDK 17** or higher
* **Maven 3.8+**
* **WiX Toolset v3.11** (Required only for building the .msi installer)

### Installation (For Users)
1. Download the latest `DailyTaskTracker.msi` from the [Releases](#) page.
2. Run the installer and follow the prompts.
3. Launch the app from your Desktop or Start Menu.

### Development (For Developers)
To run the project locally from source:

```bash
# 1. Clone the repository
git clone [https://github.com/your-username/daily-task-tracker.git](https://github.com/your-username/daily-task-tracker.git)

# 2. Navigate to project root
cd daily-task-tracker

# 3. Build and run
mvn clean javafx:run
