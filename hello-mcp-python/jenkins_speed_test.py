import requests
import time
import json
import threading
import tkinter as tk
from tkinter import ttk
import matplotlib.pyplot as plt

def load_config(path="jenkins_config.json"):
    with open(path, "r") as f:
        return json.load(f)

def test_jenkins_api_speed(duration_sec=180):
    config = load_config()
    url = f"{config['url']}/api/json"
    auth = (config['username'], config['token'])

    start_time = time.time()
    response_times = []
    error_count = 0
    total_requests = 0

    while time.time() - start_time < duration_sec:
        req_start = time.time()
        try:
            response = requests.get(url, auth=auth, timeout=10)
            resp_time = time.time() - req_start
            response_times.append(resp_time)
            if response.status_code != 200:
                error_count += 1
        except Exception:
            error_count += 1
        total_requests += 1

    if response_times:
        avg_time = sum(response_times) / len(response_times)
        min_time = min(response_times)
        max_time = max(response_times)
    else:
        avg_time = min_time = max_time = 0

    print("\n--- Jenkins API Speed Test KPI ---")
    print(f"Total requests: {total_requests}")
    print(f"Successful requests: {total_requests - error_count}")
    print(f"Error count: {error_count}")
    print(f"Average response time: {avg_time:.4f} seconds")
    print(f"Min response time: {min_time:.4f} seconds")
    print(f"Max response time: {max_time:.4f} seconds")

class JenkinsSpeedTestGUI:
    def __init__(self, duration_sec=180):
        self.duration_sec = duration_sec
        self.response_times = []
        self.error_count = 0
        self.total_requests = 0
        self.start_time = None
        self.running = False
        self.root = tk.Tk()
        self.root.title("Jenkins API Speed Test")
        self.status_label = ttk.Label(self.root, text="Starting...", font=("Arial", 14))
        self.status_label.pack(padx=10, pady=10)
        self.progress = ttk.Progressbar(self.root, length=300, mode='determinate', maximum=duration_sec)
        self.progress.pack(padx=10, pady=10)
        self.root.protocol("WM_DELETE_WINDOW", self.on_close)
        self.fig = None
        self.ax = None
        self.graph_initialized = False

    def update_gui(self):
        if self.start_time is None:
            elapsed = 0
        else:
            elapsed = int(time.time() - self.start_time)
        # Calculate requests per second
        rps = self.total_requests / elapsed if elapsed > 0 else 0
        if self.response_times:
            avg_time = sum(self.response_times) / len(self.response_times)
            min_time = min(self.response_times)
            max_time = max(self.response_times)
        else:
            avg_time = min_time = max_time = 0
        self.status_label.config(
            text=(
                f"Elapsed: {elapsed}s / {self.duration_sec}s\n"
                f"Total: {self.total_requests}\n"
                f"Success: {self.total_requests - self.error_count}\n"
                f"Errors: {self.error_count}\n"
                f"Requests/sec: {rps:.2f}\n"
                f"Avg: {avg_time:.4f}s Min: {min_time:.4f}s Max: {max_time:.4f}s"
            )
        )
        self.progress['value'] = elapsed
        self.update_graph()
        if self.running and elapsed < self.duration_sec:
            self.root.after(1000, self.update_gui)

    def update_graph(self):
        if not self.graph_initialized:
            plt.ion()
            self.fig, self.ax = plt.subplots(figsize=(10, 5))
            self.ax.set_title("Jenkins API Response Time Over Test Duration")
            self.ax.set_xlabel("Request Number (seconds)")
            self.ax.set_ylabel("Response Time (seconds)")
            self.ax.grid(True)
            self.graph_initialized = True
        self.ax.clear()
        self.ax.set_title("Jenkins API Response Time Over Test Duration")
        self.ax.set_xlabel("Request Number (seconds)")
        self.ax.set_ylabel("Response Time (seconds)")
        self.ax.grid(True)
        elapsed_times = [i for i in range(len(self.response_times))]
        self.ax.plot(elapsed_times, self.response_times, marker='o')
        self.fig.canvas.draw()
        self.fig.canvas.flush_events()

    def run_test(self):
        config = load_config()
        url = f"{config['url']}/api/json"
        auth = (config['username'], config['token'])
        self.start_time = time.time()
        self.running = True
        while time.time() - self.start_time < self.duration_sec and self.running:
            req_start = time.time()
            try:
                response = requests.get(url, auth=auth, timeout=10)
                resp_time = time.time() - req_start
                self.response_times.append(resp_time)
                if response.status_code != 200:
                    self.error_count += 1
            except Exception:
                self.error_count += 1
            self.total_requests += 1
        self.running = False
        plt.ioff()
        self.show_graph()

    def show_graph(self):
        elapsed_times = [i for i in range(len(self.response_times))]
        # Calculate metrics for annotation
        total = self.total_requests
        success = self.total_requests - self.error_count
        errors = self.error_count
        rps = self.total_requests / (self.duration_sec if self.duration_sec > 0 else 1)
        if self.response_times:
            avg_time = sum(self.response_times) / len(self.response_times)
            min_time = min(self.response_times)
            max_time = max(self.response_times)
        else:
            avg_time = min_time = max_time = 0

        plt.figure(figsize=(10, 6))
        plt.plot(elapsed_times, self.response_times, marker='o')
        plt.title("Jenkins API Response Time Over Test Duration")
        plt.xlabel("Request Number (seconds)")
        plt.ylabel("Response Time (seconds)")
        plt.grid(True)
        plt.tight_layout()

        # Add metrics as text box in the plot
        metrics_text = (
            f"Total requests: {total}\n"
            f"Successful requests: {success}\n"
            f"Error count: {errors}\n"
            f"Requests/sec: {rps:.2f}\n"
            f"Avg response time: {avg_time:.4f}s\n"
            f"Min response time: {min_time:.4f}s\n"
            f"Max response time: {max_time:.4f}s"
        )
        plt.gca().text(
            0.99, 0.01, metrics_text,
            verticalalignment='bottom', horizontalalignment='right',
            transform=plt.gca().transAxes,
            bbox=dict(facecolor='white', alpha=0.7),
            fontsize=10
        )

        plt.savefig("jenkins_speed_metrics.png")
        plt.show()
        self.root.quit()

    def on_close(self):
        self.running = False
        self.root.quit()

    def start(self):
        self.start_time = time.time()  # Ensure start_time is set before GUI updates
        threading.Thread(target=self.run_test, daemon=True).start()
        self.update_gui()
        self.root.mainloop()

if __name__ == "__main__":
    JenkinsSpeedTestGUI(duration_sec=180).start()

