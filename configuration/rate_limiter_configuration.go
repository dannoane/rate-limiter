package configuration

import (
	"encoding/json"
	"os"
)

type RateLimiterConfiguration struct {
	TargetHost string `json:"targetHost"`
	TargetPort string `json:"targetPort"`
}

func LoadConfiguration() RateLimiterConfiguration {
	data, err := os.ReadFile("./configuration.json")

	if err != nil {
		panic(err)
	}

	config := RateLimiterConfiguration{}
	err = json.Unmarshal(data, &config)

	if err != nil {
		panic(err)
	}

	return config
}
