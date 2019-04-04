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
parent <- paste('output',sep='/')
bench <- 'Sum_jmhType'
#-----------------------------------------------------------------
data <- read.accuracy(parent.folder=parent,benchmark=bench)
summary(data)
#-----------------------------------------------------------------
