@ar;
while(my $line = <>) { 
    $line =~ s/\R//g;
    push(@ar, $line)
}
my @sorted_indices =
  sort {
      @first_array = split ' ', @ar[$a];
      $first = $first_array[2];
      @second_array = split ' ', @ar[$b];
      $second = $second_array[2];
      $first cmp $second
      or $a <=> $b } 0 .. $#ar;
my @sorted = @ar[@sorted_indices]; # do a slice
print join("\n",@sorted) . "\n"

