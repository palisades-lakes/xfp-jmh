# xfp-jmh
# palisades dot lakes at gmail dot com
# version 2019-04-04
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
data.folder <- paste('output',sep='/')
file.prefix <- 'Sums-20190403-225354'
#-----------------------------------------------------------------
data <- read.runtimes(prefix=file.prefix)
summary(data)
#-----------------------------------------------------------------
runtime.log.log.plot(
  data=data, 
  prefix=file.prefix,
  plot.folder=data.folder) 
