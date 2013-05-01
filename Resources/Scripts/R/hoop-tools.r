
bar <- 1;

debug <- function(aMessage) 
{
  cat ("<HoopR> ");
  cat ("[");
  cat (bar);
  cat ("] ");
  cat (aMessage);
  cat ("\n");

  bar <<- (bar+1);
}
