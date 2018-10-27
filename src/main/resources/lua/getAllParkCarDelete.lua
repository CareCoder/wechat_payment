local vals = redis.call('hget',KEYS[1],ARGV[1])

redis.call('hdel',KEYS[1],ARGV[1])

return vals