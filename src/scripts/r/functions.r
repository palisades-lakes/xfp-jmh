# xfp-jmh
# palisades dot lakes at gmail dot com
# version 2019-04-05
#-----------------------------------------------------------------
# libraries
#-----------------------------------------------------------------
require('ggplot2')
require('scales')
require('knitr')
require('kableExtra')
#-----------------------------------------------------------------
algorithm.colors <- c(
  'DoubleAccumulator'='#1b9e7788',
  'KahanAccumulator'='#b6663888',
  'KahanFmaAccumulator'='#a6a60088',
  'RBFAccumulator'='#e41a1c88')
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
  #print(data.folder)
  pattern <- paste('[A-Za-z]+-[0-9]+-',dt.pattern,'.csv',sep='')
  #print(pattern)
  list.files(path=data.folder,pattern=pattern,full.names=TRUE) }
#-----------------------------------------------------------------
read.accuracy <- function (
  parent.folder=paste('output',sep='/'),
  benchmarks=NULL,
  dt.pattern='[0-9]{8}-[0-9]{6}') {
  stopifnot(
    !is.null(parent.folder),
    !is.null(benchmarks),
    !is.null(dt.pattern))
  
  raw <- NULL
  for (b in benchmarks) {
    files <- accuracy.files(
      parent.folder=parent.folder,
      benchmark=b,
      dt.pattern=dt.pattern)
    for (f in files) {
      #print(f)
      tmp <- read.csv(file=f,as.is=TRUE)
      tmp$residual <- tmp$truth - tmp$est
      tmp$fresidual <- tmp$residual / max(tmp$truth,1)
      raw <- rbind(raw,tmp) } }
  
  p <- 0.05
  data <- NULL
  for (b in sort(unique(raw$benchmark))) {
    for (a in sort(unique(raw$algorithm))) {
      for (d in sort(unique(raw$dim))) {
        i <- (b==raw$benchmark)&(a==raw$algorithm)&(d==raw$dim)
        #print(summary(i))
        tmp <- raw[i,]
        #print(summary(tmp))
        qr <- quantile(x=tmp$residual,probs=c(0.05,0.50,0.95))
        qar <- quantile(x=abs(tmp$residual),probs=c(0.05,0.50,0.95))
        qfr <- quantile(x=tmp$fresidual,probs=c(0.05,0.50,0.95))
        qafr <- quantile(x=abs(tmp$fresidual),probs=c(0.05,0.50,0.95))
        r <- data.frame(
          benchmark=b,
          algorithm=a,
          dim=d,
          residual05=qr[1],
          residual50=qr[2],
          residual95=qr[3],
          abs.residual05=qar[1],
          abs.residual50=qar[2],
          abs.residual95=qar[3],
          fresidual05=qfr[1],
          fresidual50=qfr[2],
          fresidual95=qfr[3],
          abs.fresidual05=qafr[1],
          abs.fresidual50=qafr[2],
          abs.fresidual95=qafr[3])
        data <- rbind(data,r) } } }
  data$benchmark <- 
    factor(x=data$benchmark,
      levels=sort(unique(data$benchmark)),
      ordered=TRUE)
  data$algorithm <- 
    factor(x=data$algorithm,
      levels=sort(unique(data$algorithm)),
      ordered=TRUE)
  data }
#-----------------------------------------------------------------
runtime.accuracy.plot <- function(
  data=NULL, 
  prefix=NULL,
  x='ms', 
  y=NULL, 
  group='algorithm',
  facet='benchmark',
  colors=algorithm.colors,
  scales='free_y',
  ylabel=NULL,
  xlabel=x,
  width=36, 
  height=12,
  plot.folder='output') {
  stopifnot(
    !is.null(data),
    !is.null(prefix),
    !is.null(y),
    !is.null(ylabel),
    !is.null(plot.folder),
    !is.null(group),
    !is.null(facet),
    !is.null(colors))
  
  plot.file <- file.path(plot.folder,paste(prefix,'png',sep='.'))
  
  p <- 
    ggplot(data=data,aes_string(x=x,y=y))  +
    facet_wrap(as.formula(paste0('~',facet)),scales=scales) +
    theme_bw() +
    theme(plot.title = element_text(hjust = 0.5)) +
    geom_line(aes_string(group=group,color=group)) +
    scale_fill_manual(values=colors) +
    scale_color_manual(values=colors) +
    scale_x_log10() + # breaks = (1000000*c(0.01,0.1,1,10))) + 
    #scale_y_log10() + #limits=c(0.10,NA)) +
    scale_y_sqrt() + #  would use log, but  there are zeros
    geom_line(aes(group=dim),color="#88888844") + 
    ylab(ylabel) +
    xlab(xlabel) +
    ggtitle(prefix)
  
  print(plot.file)
  ggsave(plot=p, device='png',filename=plot.file,
    width=width, height=height,units='cm', dpi=300) }
#-----------------------------------------------------------------
accuracy.plot <- function(
  data=NULL, 
  prefix=NULL,
  x='dim', 
  ymin=NULL, 
  y=NULL, 
  ymax=NULL, 
  group='algorithm',
  facet='benchmark',
  colors=algorithm.colors,
  scales='free_y',
  ylabel=NULL,
  xlabel='dim',
  width=36, 
  height=12,
  plot.folder='output') {
  stopifnot(
    !is.null(data),
    !is.null(prefix),
    !is.null(ymin),
    !is.null(y),
    !is.null(ymax),
    !is.null(ylabel),
    !is.null(plot.folder),
    !is.null(group),
    !is.null(facet),
    !is.null(colors))
  
  plot.file <- file.path(
    plot.folder,
    paste(prefix,'png',sep='.'))
  
  p <- ggplot(
      data=data,
      aes_string(x=x, ymin=ymin, y=y, ymax=ymax, 
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
    #scale_y_log10() + #limits=c(0.10,NA)) +
    ylab(ylabel) +
    xlab(xlabel) +
    ggtitle(prefix)
  #+ expand_limits(y=0)
  print(plot.file)
  ggsave(p , 
    device='png', 
    file=plot.file, 
    width=width, 
    height=height, 
    units='cm', 
    dpi=300) }
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
  
  data$algorithm <- 
    gsub("xfp.java.accumulators.","",data$algorithm,fixed=TRUE)
  data$algorithm <- 
    gsub("xfp.jmh.accumulators.","",data$algorithm,fixed=TRUE)
  
  data$benchmark <- 
    factor(x=data$benchmark,
      levels=sort(unique(data$benchmark)),
      ordered=TRUE)
  data$algorithm <- 
    factor(x=data$algorithm,
      levels=sort(unique(data$algorithm)),
      ordered=TRUE)
  
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
  plot.folder='output') {
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
    ggtitle('99.9% intervals for runtime')
  #+ expand_limits(y=0)
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
