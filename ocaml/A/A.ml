type smth = { name: string; lst: smth list };;
let gift = { name = "Gift"; lst = [];};;
let box = { name = "Box"; lst = [gift];};;
let giftCtor =
    { name = "Gift"; lst = [];};;
let boxCtor =
    { name = "Box"; lst = [];};;
(* type smth = 
    | Gift of {name: string}
    | Box of {name: string; lst: smth list};;

let gift = Gift { name="Gift" };;
let box = Box { name="Box"; lst=[gift] };;
 *)


let rec string_of_smth smth =
    match smth.name with
    | "Gift" -> "Gift"
    | _ -> "Box[" ^ (String.concat ";" (List.map string_of_smth smth.lst)) ^ "]";;




let reverse_string str =
    let n = String.length str in
    String.mapi (fun i ch -> String.get str (n-1-i)) str;;


(* let value: int = 1 *)

let packing str =
    let n = String.length str in
    let rec helper i = 
        (* let _ = Printf.printf "i=%d\n" i in  *)
        if i == n then
            Some([]: smth list) 
        else
            let prev = helper(i+1) in
            (* let prev_str = if Option.is_none prev then "None" else (String.concat ";" (List.map string_of_smth (Option.get prev))) in
            let _ = Printf.printf "i=%d, prev=%s\n" i prev_str in  *)
            if Option.is_none prev then
                prev
            else
                match String.get str i with
                | 'g' -> if i == 0 then None else Some( giftCtor :: Option.get(prev))
                | 'b' -> if i == 0 then None else Some( boxCtor :: Option.get(prev))
                | 'p' -> let table = Option.get prev in
                        if List.length table >= 2 then
                        let a = List.hd table and
                        b = List.hd @@ List.tl table and
                        rest = List.tl @@ List.tl table in
                        (* let rest_str = String.concat ";" (List.map string_of_smth rest) in
                        let _ = Printf.printf "i=%d, rest=%s\n" i rest_str in *)
                        match a.name with
                        | "Box" -> if i == 0 && List.length rest > 0 then None else Some( { name = "Box"; lst = b :: a.lst } :: rest )
                        | _ -> None
                    else
                        None
                | _ -> None
    in
    helper(0);;


let s = read_line()
(* let s = "gbpbpbbbpbpppbp";; *)
(* let s = "bgbpp" *)
(* let t = "ABC";; *)
(* let lst = String.split_on_char('B') t;; *)
(* print_endline(String.concat ", " lst);; *)
(* print_endline(reverse_string(t));; *)
(* print_endline(gift.name);; *)
(* print_endline(a.lst);; *)

let packed = packing(reverse_string(s));;
let result = if Option.is_some packed then string_of_smth(List.hd (Option.get packed)) else "fail";;
(* let result = if (Option.is_some packed) && (List.length(Option.get(packed)) = 1)
             && ((List.hd(Option.get(packed))).name = "Box") then string_of_smth(List.hd(Option.get(packed)))
             else "fail";; *)

print_endline(result);;
(* print_endline(string_of_smth gift);; *)
(* print_endline(string_of_smth box);; *)