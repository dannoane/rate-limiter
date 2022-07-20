package main

import (
	"io"
	"log"
	"net/http"
)

var hopByHopHeaders = []string{
	"Connection",
	"Keep-Alive",
	"Proxy-Authenticate",
	"Proxy-Authorization",
	"TE",
	"Trailers",
	"Transfer-Encoding",
	"Upgrade",
}

func deleteHopByHopHeaders(headers http.Header) {
	for _, headerName := range hopByHopHeaders {
		headers.Del(headerName)
	}
}

func copyHeaders(dest, src http.Header) {
	for headerName, headerValues := range src {
		for _, headerValue := range headerValues {
			dest.Add(headerName, headerValue)
		}
	}
}

func setStatusCode(dest http.ResponseWriter, statusCode int) {
	dest.WriteHeader(statusCode)
}

func copyBody(dest http.ResponseWriter, body io.ReadCloser) {
	io.Copy(dest, body)
}

func handler(w http.ResponseWriter, req *http.Request) {
	log.Printf("[+] Got %s %s request path %s", req.Proto, req.Method, req.URL)

	req.URL.Host = "localhost:5000"
	req.URL.Scheme = "http"
	req.RequestURI = ""
	deleteHopByHopHeaders(req.Header)

	client := &http.Client{}
	resp, err := client.Do(req)

	if err != nil {
		log.Fatalf("[-] Client server error %s", err)
		http.Error(w, "Server error", http.StatusInternalServerError)
	}

	defer resp.Body.Close()

	deleteHopByHopHeaders(resp.Header)
	copyHeaders(w.Header(), resp.Header)
	setStatusCode(w, resp.StatusCode)
	copyBody(w, resp.Body)
}

func main() {
	http.HandleFunc("/", handler)
	log.Fatal(http.ListenAndServe(":3333", nil))
}
