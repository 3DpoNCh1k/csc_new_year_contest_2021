Inductive nat (* nat *) :=
  | O : nat
  | S : nat -> nat.

Check O.              (* 0 *)

Check S O.            (* 1 *)

Check S (S O).        (* 2 *)

Check S (S (S O)).    (* 3 *)

Fixpoint add (m n : nat) : nat :=
  match m with
  | O => n
  | S m' => S (add m' n)
  end.
  
Compute add (S (S O)) (S (S O)).

Notation "m + n" := (add m n).
  
Lemma addA : forall m n p : nat,
  (m + n) + p = m + (n + p).
Proof.
    intros m n p.
    induction m as [| m' IHm ].
    - simpl. reflexivity.
    - simpl. rewrite IHm. reflexivity.
Qed.

