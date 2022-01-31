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
  induction m as [ | m' IHm ].
  - simpl. reflexivity.
  - simpl. rewrite IHm. reflexivity.
Qed.

Lemma addn0 : forall n : nat,
  n + O = n.
Proof.
  intros n.
  induction n.
  - reflexivity.
  - simpl. rewrite IHn. reflexivity.
Qed.

Lemma addnS : forall m n : nat,
  m + (S n) = S (m + n).
Proof.
  intros m n.
  induction m; simpl.
  - reflexivity.
  - rewrite IHm. reflexivity.
Qed.

Lemma addC : forall m n : nat,
  m + n = n + m.
Proof.
  intros m n.
  induction m.
  - simpl. rewrite addn0. reflexivity.
  - simpl. rewrite IHm. rewrite addnS. reflexivity.
Qed.

Lemma addCA : forall m n p,
  m + (n + p) = n + (m + p).
Proof.
  intros m n p.
  rewrite addC.
  rewrite addA.
  rewrite (addC p m).
  reflexivity.
Qed.