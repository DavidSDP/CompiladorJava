import sys
import os
import shutil

exec_dir = os.path.dirname(os.path.realpath(sys.argv[0]))
output_dir = os.path.realpath(os.path.join(exec_dir, "..", "output"))
k68_dir = os.path.realpath(os.path.join(exec_dir, "..", "68k"))



with open(os.path.join(output_dir, "codigo_maquina_opt.txt"), "r") as fd:
		generated_code = fd.readlines()

with open(os.path.join(exec_dir, "template.X68"), "r") as fd:
    text = fd.readlines()
   
joint_text = "".join(text)
joint_code = "".join(generated_code).replace("\n", "\r\n")
final_code = joint_text.replace(";{Generate Code here}", joint_code)

result_path = os.path.join(output_dir, "final_code_opt.X68")
with open(result_path, "w") as fd:
    fd.write(final_code)

if os.path.exists(k68_dir):
    k68_file = os.path.join(k68_dir, "final_code_opt.X68")
    shutil.copy(result_path, k68_file)
