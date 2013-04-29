# http://ww2.coastal.edu/kingw/statistics/R-tutorials/singlesample.html

trim.leading <- function (x)  sub("^\\s+", "", x)

# returns string w/o trailing whitespace
trim.trailing <- function (x) sub("\\s+$", "", x)

# returns string w/o leading or trailing whitespace
trim <- function (x) gsub("^\\s+|\\s+$", "", x)

include <- function (aFile)
{
	frame_files <- lapply(sys.frames(), function(x) x$ofile)
	frame_files <- Filter(Negate(is.null), frame_files)
	PATH <- dirname(frame_files[[length(frame_files)]])

	pre <- paste (trim (PATH),"/",sep="");
	final <- paste (pre,aFile,sep = "");

	cat ("Loading: ");
	cat (final);
	cat (" ...\n");

	source (final);
}

include ("hoop-tools.r");

debug ("Running one sample t-test unit test ...")

# read the generated dataset as we made it in the Hoop statistics panel
fpe <- read.csv(file="c:\\incoming\\hoop-generated.dat",head=TRUE,sep="\t")

debug(summary (fpe$Generated));
debug(names(fpe));

debug ("Assigning data ...");

data = fpe$Generated;

debug ("Running qqnorm ...");

# qqnorm(data);		### output not shown

debug ("Running qqline ...");

# qqline(data);		### output not shown

debug ("Running plot ...");

# plot(density(data));	### output not shown

debug ("Running shapiro.test ...");

shapiro.test(data);	### output not shown

# Run One Sample t-test
debug ("------------------------------------------------------------");
print (t.test(data, mu=250, alternative="two.sided"));
debug ("------------------------------------------------------------");

debug ("All done");

