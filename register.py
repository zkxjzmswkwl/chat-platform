import requests,sys
payload = {"username": sys.argv[1], "password": sys.argv[2]}
r = requests.post("http://localhost:9092/api/users", json=payload)
print(f"[{r.status_code}] {r.text}")