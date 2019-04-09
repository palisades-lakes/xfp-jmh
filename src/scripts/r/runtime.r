# xfp-jmh
# palisades dot lakes at gmail dot com
# version 2019-04-08
#-----------------------------------------------------------------
if (file.exists('e:/porta/projects/xfp-jmh')) {
  setwd('e:/porta/projects/xfp-jmh')
  model <- '20ERCTO1WW' # P70
} else {
  setwd('c:/porta/projects/xfp-jmh')
  model <- '20HRCTO1WW' # X1
}
source('src/scripts/r/functions.r')
#-----------------------------------------------------------------
prefix <- 'Sums-20190406-223001'
#-----------------------------------------------------------------
runtime <- read.runtimes(prefix=prefix)
summary(runtime)
#-----------------------------------------------------------------
options(warn=2)
#options(error=utils::dump.frames)
options(error = function() traceback(2))
runtime.log.log.plot(data=runtime,prefix=prefix) 
