# read from 3d line
# to lowercase letters
# split by ',' remove 4th field
# remove last line 
# remove lines with <= 3 commas
# to uppercase right bound letter
# stable sort lines by 5th field (sep = ',') treat strings as number (numeric sort)
# pretty print and calc avg age and avg salary
@lines = <>;
splice(@lines,0,2);
tr/A-Z/a-z/ for @lines; 
s/^(([^,]*,){3})[^,]*,(.*)/\1\3/ for @lines;
pop(@lines);
@lines = grep(!/^[^,]*(,[^,]*){0,3}$/, @lines); 
s/\b(.)/\u\1/g for @lines;
use sort 'stable';
@sorted_lines = sort { 
    $key1 = "";
    if ($a =~ m/^[^,]*(,([^,]*)){4}.*/) {
        $key1 = $2;
    }
    $key2 = "";
    if ($b =~ m/^[^,]*(,([^,]*)){4}.*/) {
        $key2 = $2;
    }
    $key1 <=> $key2;
} @lines;
$index = 0;
$age_sum = 0;
$salary_sum = 0;
foreach $line (@sorted_lines) {
    $index++;
    ($name, $surname, $age, $position, $salary) = ($line =~ m/^([^,]*),([^,]*),([^,]*),([^,]*),([^,]*).*\R/);
    $age_sum += $age;
    $salary_sum += $salary;
    printf("%4d | %-16s | %-3s | %-24s | %-9s\n", $index, "$surname $name", $age, $position, $salary)
}
printf("\nAvg age: %d\nAvg salary: %d\n", $index? $age_sum / $index: -(2**31 - 1), $index? $salary_sum / $index: -(2**31 - 1))