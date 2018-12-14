all: guide.pdf

%.pdf : %.md
	~/.cabal/bin/pandoc $< -s -t latex -o $@
view: guide.pdf
	epdfview $<
