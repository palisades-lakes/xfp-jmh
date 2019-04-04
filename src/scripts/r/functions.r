# xfp-jmh
# palisades dot lakes at gmail dot com
# version 2019-04-04
#-----------------------------------------------------------------
# libraries
#-----------------------------------------------------------------
require('ggplot2')
require('scales')
require('knitr')
require('kableExtra')
#-----------------------------------------------------------------
algorithm.colors <- c(
  'DoubleAccumulator'='#1b9e77',
  'KahanAccumulator'='#b66638',
  'KahanFmaAccumulator'='#a6a600',
  'RBFAccumulator'='#e41a1c')
#-----------------------------------------------------------------
accuracy.files <- function (
  parent.folder='output',
  benchmark=NULL,
  dt.pattern='[0-9]{8}-[0-9]{6}') {
  stopifnot(
    !is.null(parent.folder),
    !is.null(benchmark),
    !is.null(dt.pattern))
  
  data.folder <- paste(parent.folder,benchmark,sep='/')
  print(data.folder)
  pattern <- paste('[A-Za-z]+-[0-9]+-',dt.pattern,'.csv',sep='')
  print(pattern)
  list.files(path=data.folder,pattern=pattern,full.names=TRUE) }
#-----------------------------------------------------------------
read.accuracy <- function (
  parent.folder=paste('output',sep='/'),
  benchmark=NULL,
  dt.pattern='[0-9]{8}-[0-9]{6}') {
  stopifnot(
    !is.null(parent.folder),
    !is.null(benchmark),
    !is.null(dt.pattern))
  
  files <- accuracy.files(
    parent.folder=parent.folder,
    benchmark=benchmark,
    dt.pattern=dt.pattern)
print(files)
 
#data <- read.csv(file=data.file,as.is=TRUE)
#  colnames(data)[which(names(data) == "Param..dim")] <- "dim"
#  colnames(data)[which(names(data) == "Param..className")] <- 
#    "algorithm"
#  colnames(data)[which(names(data) == "Score")] <- 
#    "ms"
#  colnames(data)[which(names(data) == "Score.Error..99.9..")] <- 
#    "delta999"
#  colnames(data)[which(names(data) == "Benchmark")] <- 
#    "benchmark"
#  summary(data)
#  data$benchmark <- 
#    gsub(".bench","",data$benchmark,fixed=TRUE)
#  data$benchmark <- 
#    gsub("xfp.jmh.","",data$benchmark,fixed=TRUE)
#  data$benchmark <- 
#    factor(data$benchmark,sort(unique(data$benchmark)))
#  data$algorithm <- 
#    gsub("xfp.java.accumulators.","",data$algorithm,fixed=TRUE)
#  data$algorithm <- 
#    gsub("xfp.jmh.accumulators.","",data$algorithm,fixed=TRUE)
#  data$algorithm <- 
#    factor(data$algorithm,sort(unique(data$algorithm)))
#  data$ms.min <- data$ms - data$delta999
#  data$ms.max <- data$ms + data$delta999
#  data 
}
#-----------------------------------------------------------------
read.runtimes <- function (
  folder=paste('output',sep='/'),
  prefix=NULL) {
  data.file <- paste(folder,paste(prefix,'csv',sep='.'),sep='/')
  data <- read.csv(file=data.file,as.is=TRUE)
  colnames(data)[which(names(data) == "Param..dim")] <- "dim"
  colnames(data)[which(names(data) == "Param..className")] <- 
    "algorithm"
  colnames(data)[which(names(data) == "Score")] <- 
    "ms"
  colnames(data)[which(names(data) == "Score.Error..99.9..")] <- 
    "delta999"
  colnames(data)[which(names(data) == "Benchmark")] <- 
    "benchmark"
  summary(data)
  data$benchmark <- 
    gsub(".bench","",data$benchmark,fixed=TRUE)
  data$benchmark <- 
    gsub("xfp.jmh.","",data$benchmark,fixed=TRUE)
  data$benchmark <- 
    factor(data$benchmark,sort(unique(data$benchmark)))
  data$algorithm <- 
    gsub("xfp.java.accumulators.","",data$algorithm,fixed=TRUE)
  data$algorithm <- 
    gsub("xfp.jmh.accumulators.","",data$algorithm,fixed=TRUE)
  data$algorithm <- 
    factor(data$algorithm,sort(unique(data$algorithm)))
  data$ms.min <- data$ms - data$delta999
  data$ms.max <- data$ms + data$delta999
  data }
#-----------------------------------------------------------------
runtime.log.log.plot <- function(
  data=NULL, 
  prefix=NULL,
  x='dim', 
  y='ms', 
  ymin='ms.min', 
  ymax='ms.max', 
  group='algorithm',
  facet='benchmark',
  colors=algorithm.colors,
  scales='fixed', #'free_y',
  ylabel='milliseconds',
  xlabel='dim',
  width=36, 
  height=12,
  plot.folder=NULL) {
  stopifnot(
    !is.null(data),
    !is.null(prefix),
    !is.null(plot.folder),
    !is.null(group),
    #!is.null(facet),
    !is.null(colors))
  
  plot.file <- file.path(
    plot.folder,
    paste(prefix,'png',sep='.'))
  
  p <- ggplot(
      data=data,
      aes_string(
        x=x, ymin=ymin, y=y, ymax=ymax, 
        group=group,fill=group,color=group))  +
    facet_wrap(as.formula(paste0('~',facet)),scales=scales) +
    theme_bw() +
    theme(plot.title = element_text(hjust = 0.5)) +
    #theme(
    #  axis.text.x=element_text(angle=-90,hjust=0,vjust=0.5),
    #  axis.title.x=element_blank()) + 
    geom_ribbon(aes_string(ymin=ymin, ymax=ymax, fill=group)) +
    geom_line(aes_string(y = ymin)) + 
    geom_line(aes_string(y = y)) + 
    geom_line(aes_string(y = ymax)) +   
    scale_fill_manual(values=colors) +
    scale_color_manual(values=colors) +
    scale_x_log10() + # breaks = (1000000*c(0.01,0.1,1,10))) + 
    scale_y_log10() + #limits=c(0.10,NA)) +
    ylab(ylabel) +
    xlab(xlabel) +
    ggtitle('99.9% intervals for runtime') +
    expand_limits(y=0); 
  print(plot.file)
  ggsave(p , 
    device='png', 
    file=plot.file, 
    width=width, 
    height=height, 
    units='cm', 
    dpi=300) }
#-----------------------------------------------------------------
html.table <- function(data,fname,n) {
  html.file <- file(
    description=file.path(
      plot.folder,
      paste(fname,'html',sep='.')),
    encoding='UTF-8',
    open='wb')
  writeLines(
    kable(
      data[order(data$algorithm),],
      format='html',
      digits=1,
      caption=paste('milliseconds for',n,'intersection tests'),
      row.names=FALSE,
      col.names = c('algorithm','0.05','0.50','0.95','mean')),
    con=html.file,
    sep='\n')
  close(html.file) }
#-----------------------------------------------------------------
md.table <- function(data,fname,n) {
  md.file <- file(
    description=file.path(
      plot.folder,
      paste(fname,'md',sep='.')),
    encoding='UTF-8',
    open='wb')
  writeLines(
    kable(
      data[order(data$benchmark,data$nmethods,data$algorithm),],
      format='markdown',
      digits=2,
      caption=paste('milliseconds for',n,'intersection tests'),
      row.names=FALSE,
      col.names = c('benchmark','algorithm','nmethods',
        '0.05','0.50','0.95','mean',
        'overhead 0.05','overhead 0.50','overhead 0.95','overhead mean',
        'ns per op','overhead ns per op')),
    con=md.file,
    sep='\n') 
  close(md.file) }
#-----------------------------------------------------------------
quantile.log.log.plot <- function(
  data=NULL, 
  fname=NULL,
  ymin='lower.q', 
  y='median', 
  ymax='upper.q',
  group=NULL,
  facet=NULL,
  colors=NULL,
  scales='fixed', #'free_y',
  ylabel='milliseconds',
  width=36, 
  height=21,
  plot.folder=NULL) {
  stopifnot(
    !is.null(data),
    !is.null(fname),
    !is.null(plot.folder),
    !is.null(group),
    !is.null(facet),
    !is.null(colors))
  
  plot.file <- file.path(
    plot.folder,
    paste(fname,group,facet,ylabel,'quantiles','png',sep='.'))
  
  p <- ggplot(
      data=data,
      aes_string(
        x='nelements',  
        ymin=ymin, y=y, ymax=ymax, 
        group=group,
        fill=group, 
        color=group))  +
    facet_wrap(as.formula(paste0('~',facet)),scales=scales) +
    theme_bw() +
    theme(plot.title = element_text(hjust = 0.5)) +
    theme(
      axis.text.x=element_text(angle=-90,hjust=0,vjust=0.5),
      axis.title.x=element_blank()) + 
    geom_ribbon(aes_string(ymin = ymin, ymax = ymax, fill = group)) +
    geom_line(aes_string(y = ymin)) + 
    geom_line(aes_string(y = y)) + 
    geom_line(aes_string(y = ymax)) +   
    scale_fill_manual(values=colors) +
    scale_color_manual(values=colors) +
    scale_x_log10(breaks = (1000000*c(0.01,0.1,1,10))) + 
    scale_y_log10(limits=c(0.10,NA)) +
    ylab(ylabel) +
    ggtitle(paste('[0.05,0.50,0.95] quantiles for', ylabel)) +
    expand_limits(y=0); 
  print(plot.file)
  ggsave(p , 
    device='png', 
    file=plot.file, 
    width=width, 
    height=height, 
    units='cm', 
    dpi=300) }
#-----------------------------------------------------------------
