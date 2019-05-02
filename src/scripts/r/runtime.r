# xfp-jmh
# palisades dot lakes at gmail dot com
# version 2019-05-01
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
#prefix <- 'Sums-20190410-213030'
#prefix <- 'Sums-20190414-144041'
prefix <- 'PartialSums-20190430-212132'
#-----------------------------------------------------------------
runtime <- read.runtimes(prefix=prefix)
summary(runtime)
#-----------------------------------------------------------------
options(warn=2)
#options(error=utils::dump.frames)
options(error = function() traceback(2))

source('src/scripts/r/functions.r')
runtime.plot(data=runtime,prefix=prefix,xscale=NULL,yscale=NULL) 
runtime.plot(data=runtime,prefix=paste(prefix,'log','log',sep='-')) 
