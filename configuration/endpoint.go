package configuration

type LimitingStrategy string
type LimitByCriteria string

const (
	TokenBucket          LimitingStrategy = "tokenBucket"
	LeakingBucket        LimitingStrategy = "leakingBucket"
	FixedWindowCounter   LimitingStrategy = "fixedWindowCounter"
	SlidingWindowLog     LimitingStrategy = "slidingWindowLog"
	SlidingWindowCounter LimitingStrategy = "slidingWindowCounter"
)

const (
	AllRequests LimitByCriteria = "allRequests"
	IP          LimitByCriteria = "ip"
)

type Endpoint struct {
	Path             string           `json:"path"`
	NumberOfRequests uint64           `json:"numberOfRequests"`
	Window           uint64           `json:"window"`
	Strategy         LimitingStrategy `json:"strategy"`
	LimitBy          LimitByCriteria  `json:"limitBy"`
	Endpoints        []Endpoint       `json:"endpoints"`
}
